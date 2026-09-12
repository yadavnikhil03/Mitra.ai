(function () {
  'use strict'

  var canvas = document.getElementById('face')
  var statusEl = document.getElementById('status')

  var app = null
  var model = null
  var mouthTarget = 0
  var mouthNow = 0
  var focusX = 0
  var focusY = 0
  var lastPointer = 0
  var running = false
  var state = 'idle'
  var stateTime = 0
  var nodStarted = 0

  function showStatus(text) {
    statusEl.style.display = text ? 'block' : 'none'
    statusEl.textContent = text
  }

  function notify(type, message) {
    if (!window.MitraAndroid) return
    var fn = type === 'error' ? window.MitraAndroid.onFaceError : window.MitraAndroid.onFaceReady
    if (fn) {
      try { fn(message || '') } catch (e) {}
    }
  }

  function resize() {
    var dpr = Math.min(window.devicePixelRatio || 1, 2)
    var w = Math.max(1, Math.round(canvas.clientWidth * dpr))
    var h = Math.max(1, Math.round(canvas.clientHeight * dpr))
    canvas.width = w
    canvas.height = h
    if (app) app.renderer.resize(w, h)
    if (model) fitModel()
  }

  function fitModel() {
    var w = canvas.clientWidth
    var h = canvas.clientHeight
    if (!model || w === 0 || h === 0) return
    var isPortrait = h > 1.15 * w
    if (isPortrait) {
      model.anchor.set(0.5, 1)
      var s = Math.min(w / model.width, h / model.height)
      model.scale.set(s, s)
      model.x = w / 2
      model.y = h
    } else {
      model.anchor.set(0.5, 0.5)
      var sc = Math.min(w / model.width, h / model.height) * 0.9
      model.scale.set(sc, sc)
      model.x = w / 2
      model.y = h / 2
    }
  }

  function setParameter(id, v) {
    if (!model || !model.internalModel || !model.internalModel.coreModel) return
    try {
      model.internalModel.coreModel.setParameterValueById(id, v, 2)
    } catch (e) {}
  }

  function lookAt(x, y) {
    var w = canvas.clientWidth
    var h = canvas.clientHeight
    if (w === 0 || h === 0) return
    var nx = Math.max(-1, Math.min(1, x === undefined ? focusX : x))
    var ny = Math.max(-1, Math.min(1, y === undefined ? focusY : y))
    focusX = nx
    focusY = ny
    if (model && model.internalModel && model.internalModel.focusController) {
      model.internalModel.focusController.focus(nx, ny)
    }
  }

  function pointerToFocus(e) {
    lastPointer = Date.now()
    var rect = canvas.getBoundingClientRect()
    var rx = (e.clientX - rect.left) / Math.max(1, rect.width)
    var ry = (e.clientY - rect.top) / Math.max(1, rect.height)
    lookAt(rx * 2 - 1, 1 - ry * 2)
  }

  function hookPointer() {
    canvas.addEventListener('pointerdown', function (e) { pointerToFocus(e) })
    canvas.addEventListener('pointermove', function (e) {
      pointerToFocus(e)
    })
  }

  function pump() {
    requestAnimationFrame(pump)
    var t = performance.now() / 1000
    mouthNow += (mouthTarget - mouthNow) * 0.30

    var headX = 0
    var headY = 0
    var headZ = 0
    var browLY = 0
    var browRY = 0
    var mouth = 0
    var bodyX = Math.sin(t * 0.8) * 0.02
    var bodyY = Math.sin(t * 0.55) * 0.012

    switch (state) {
      case 'listening':
        headX = 0.045 + Math.sin(t * 0.7) * 0.015
        headY = 0.035
        headZ = -0.05 + Math.sin(t * 0.5) * 0.01
        browLY = 0.1 + Math.sin(t * 0.9) * 0.02
        browRY = 0.1 + Math.sin(t * 0.9) * 0.02
        if (t - nodStarted > 3.4) {
          nodStarted = t
        }
        var nodK = Math.max(0, 1 - (t - nodStarted) / 0.7)
        headY += nodK * 0.05
        break
      case 'thinking':
        headX = Math.sin(t * 0.35) * 0.02
        headY = -0.12
        headZ = Math.sin(t * 0.25) * 0.02
        browLY = -0.14
        browRY = -0.14
        break
      case 'speaking':
        headX = Math.sin(t * 2.9) * 0.028 + mouthNow * 0.02
        headY = 0.02 + Math.sin(t * 1.7) * 0.018
        headZ = Math.sin(t * 2.2) * 0.02
        browLY = Math.sin(t * 6.5) * 0.06 * mouthNow
        browRY = Math.sin(t * 6.5 + 1.1) * 0.06 * mouthNow
        mouth = mouthNow
        break
      case 'idle':
      default:
        headX = Math.sin(t * 0.5) * 0.02
        headY = Math.sin(t * 0.72) * 0.015
        headZ = Math.sin(t * 0.4) * 0.012
        break
    }

    setParameter('ParamAngleX', headX)
    setParameter('ParamAngleY', headY)
    setParameter('ParamAngleZ', headZ)
    setParameter('ParamBrowLY', browLY)
    setParameter('ParamBrowRY', browRY)
    setParameter('ParamBodyAngleX', bodyX)
    setParameter('ParamBodyAngleY', bodyY)
    setParameter('ParamMouthOpenY', mouth)
  }

  function playIdle() {
    if (!model) return
    try { model.motion('Idle', 0) } catch (e) {}
  }

  function saccade() {
    if (!model) return
    if (Date.now() - lastPointer < 2600) return
    var relaxed = 0.45
    if (state === 'listening') relaxed = 0.2
    if (state === 'thinking') relaxed = 0.15
    var dx = (Math.random() - 0.5) * relaxed
    var dy = (Math.random() - 0.5) * relaxed
    var nx = Math.max(-0.35, Math.min(0.35, focusX * 0.5 + dx))
    var ny = Math.max(-0.30, Math.min(0.30, focusY * 0.5 + dy))
    lookAt(nx, ny)
  }

  function boot() {
    if (running) return
    if (!window.PIXI || !window.Live2DCubismCore) {
      showStatus('live2d lib missing')
      notify('error', 'live2d libs missing')
      return
    }
    resize()
    try {
      app = new PIXI.Application({
        view: canvas,
        autoStart: true,
        antialias: true,
        backgroundAlpha: 0,
        resolution: 1
      })
      window.addEventListener('resize', resize)
      PIXI.live2d.Live2DModel.from('Model/Hiyori/Hiyori.model3.json', {
        autoUpdate: true,
        autoInteract: false,
        motionPreload: 'IDLE'
      }).then(function (m) {
        model = m
        app.stage.addChild(model)
        fitModel()
        hookPointer()
        playIdle()
        requestAnimationFrame(pump)
        setInterval(playIdle, 12000)
        setInterval(saccade, 4200)
        running = true
        showStatus('')
        notify('ready', '')
      }).catch(function (err) {
        var msg = String(err && err.message ? err.message : err)
        showStatus(msg)
        notify('error', msg)
      })
    } catch (err) {
      var msg = String(err && err.message ? err.message : err)
      showStatus(msg)
      notify('error', msg)
    }
  }

  window.MitraFace = {
    setMouth: function (open) {
      var v = isFinite(open) ? open : 0
      mouthTarget = Math.max(0, Math.min(1, v))
      if (v > 0.05 && state !== 'listening' && state !== 'thinking') {
        if (Math.abs(focusX) > 0.25 || focusY > 0.3 || focusY < -0.1) {
          lookAt(focusX * 0.5, focusY * 0.5 + 0.1)
        }
      }
    },
    setState: function (next) {
      if (!next || state === next) return
      if (next === 'idle' || next === 'listening' || next === 'thinking' || next === 'speaking') {
        state = next
        stateTime = performance.now() / 1000
        if (state !== 'speaking') mouthTarget = 0
      }
    },
    lookAt: function (x, y) {
      if (isFinite(x)) lookAt(Math.max(-1, Math.min(1, x)), isFinite(y) ? Math.max(-1, Math.min(1, y)) : 0)
    },
    getState: function () { return state },
    isReady: function () { return running }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', boot)
  } else {
    boot()
  }
})()