package com.belajar.api.kotlin.constant

object StatusMessage {
    const val SUCCESS: String = "Success"
    const val SUCCESS_CREATE_USER = "Account successfully created"
    const val SUCCESS_CREATE: String = "Data successfully created"
    const val SUCCESS_RETRIEVE: String = "Data successfully retrieved"
    const val SUCCESS_UPDATE: String = "Data successfully updated"
    const val SUCCESS_DELETE: String = "Data successfully deleted"
    const val SUCCESS_DISABLED: String = "Data successfully disabled"
    const val SUCCESS_ENABLED: String = "Data successfully enabled"
    const val SUCCESS_CREATE_LIST: String = "List of data successfully created"
    const val SUCCESS_RETRIEVE_LIST: String = "List of data successfully retrieved"
    const val NOT_FOUND: String = "Data not found"
    const val BAD_REQUEST: String = "Bad request"
    const val INTERNAL_SERVER_ERROR: String = "Internal server error"
    const val USERNAME_NOT_FOUND: String = "Username not found"
    const val USERNAME_BEEN_TAKEN: String = "Username has already been taken"
    const val EMAIL_TAKEN: String = "Email has already been taken"
    const val USER_NOT_FOUND: String = "User not found"
    const val CUSTOMER_NOT_FOUND: String = "Customer not found"
    const val TABLE_NOT_FOUND: String = "Table not found"
    const val ERROR_CREATING_JWT: String = "Error creating JWT"
    const val SUCCESS_LOGIN: String = "Login success"
    const val UNAUTHORIZED: String = "Unauthorized"
    const val CONFLICT: String = "Data has reference to another table"
    const val ACCESS_DENIED: String = "Access Denied"
    const val IMAGE_NOT_FOUND: String = "Image not found"
    const val ERROR_INVALID_JWT: String = "Invalid JWT"
}