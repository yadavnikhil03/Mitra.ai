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
    model.anchor.set(0.5, 0.5)
    var s = Math.min(w / model.width, h / model.height) * 0.92
    model.scale.set(s, s)
    model.x = w / 2
    model.y = h / 2
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
      if (e.pointerType === 'touch') {
        pointerToFocus(e)
      } else {
        pointerToFocus(e)
      }
    })
  }

  function pump() {
    requestAnimationFrame(pump)
    mouthNow += (mouthTarget - mouthNow) * 0.30
    if (model && model.internalModel && model.internalModel.coreModel) {
      model.internalModel.coreModel.setParameterValueById('ParamMouthOpenY', mouthNow, 2)
    }
  }

  function playIdle() {
    if (!model) return
    try { model.motion('Idle', 0) } catch (e) {}
  }

  function saccade() {
    if (!model) return
    if (Date.now() - lastPointer < 2600) return
    var dx = (Math.random() - 0.5) * 0.7
    var dy = (Math.random() - 0.5) * 0.7
    var nx = Math.max(-0.35, Math.min(0.35, focusX * 0.6 + dx))
    var ny = Math.max(-0.30, Math.min(0.30, focusY * 0.6 + dy))
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
      if (v > 0.05) {
        if (Math.abs(focusX) > 0.25 || focusY > 0.3 || focusY < -0.1) {
          lookAt(focusX * 0.5, focusY * 0.5 + 0.1)
        }
      }
    },
    lookAt: function (x, y) {
      if (isFinite(x)) lookAt(Math.max(-1, Math.min(1, x)), isFinite(y) ? Math.max(-1, Math.min(1, y)) : 0)
    },
    isReady: function () { return running }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', boot)
  } else {
    boot()
  }
})()