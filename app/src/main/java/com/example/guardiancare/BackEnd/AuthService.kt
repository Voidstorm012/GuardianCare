package com.example.guardiancare.BackEnd

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object AuthService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(io.ktor.client.plugins.logging.Logging) {
            level = io.ktor.client.plugins.logging.LogLevel.ALL
        }
    }

//    private const val BASE_URL = "http://192.168.x.xx:3000/api/auth" //If you are testing on a real phone connected to the same Wi-Fi, change the IP address to your phone's IP address
    private const val BASE_URL = "http://10.0.2.2:3000/api/auth" // if using android studio device emulator (for mac os... might be diff for windows )

    @Serializable
    data class RegisterRequest(val name: String, val email: String, val password: String, val role: String)

    suspend fun register(name: String, email: String, password: String, role: String): String? {
        return try {
            val response: HttpResponse = client.post("$BASE_URL/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(name, email, password, role))
            }

            if (response.status == HttpStatusCode.Created) {
                response.body()
            } else {
                "Error: Server responded with ${response.status}"
            }
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }


//    @Serializable
//    data class LoginRequest(val email: String, val password: String)
//
//    suspend fun login(email: String, password: String): String? {
//        return try {
//            val response: HttpResponse = client.post("$BASE_URL/login") {
//                contentType(ContentType.Application.Json)
//                setBody(LoginRequest(email, password))
//            }
//            response.body()
//        } catch (e: Exception) {
//            "Error: ${e.localizedMessage}"
//        }
//    }
}