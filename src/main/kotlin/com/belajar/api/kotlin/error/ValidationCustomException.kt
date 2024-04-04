package com.belajar.api.kotlin.error

class ValidationCustomException(message: String = "", val path: String = ""): Exception(message) {
}