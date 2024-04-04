package com.belajar.api.kotlin.exception

class ValidationCustomException(message: String = "", val path: String = ""): Exception(message) {
}