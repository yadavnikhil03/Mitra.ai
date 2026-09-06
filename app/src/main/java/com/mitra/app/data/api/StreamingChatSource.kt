package com.mitra.app.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Converts the raw streaming [ResponseBody] from the /chat endpoint into a
 * [Flow] of text chunks, mirroring the web app's ReadableStream reader loop.
 *
 * The backend streams plain UTF-8 text — each write() call from the Python
 * StreamingResponse becomes one or more chunks here. We accumulate them as
 * they arrive and emit each incremental token downstream.
 */
object StreamingChatSource {

    fun stream(response: Response<ResponseBody>): Flow<String> = flow {
        val body = response.body() ?: return@flow
        val source = body.source()
        val buffer = okio.Buffer()
        try {
            while (!source.exhausted()) {
                val bytesRead = source.read(buffer, 8192)
                if (bytesRead == -1L) break
                val chunk = buffer.readUtf8()
                if (chunk.isNotEmpty()) emit(chunk)
            }
        } finally {
            body.close()
        }
    }.flowOn(Dispatchers.IO)
}
