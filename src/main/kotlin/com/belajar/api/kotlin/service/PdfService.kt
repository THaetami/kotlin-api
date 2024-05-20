package com.belajar.api.kotlin.service

import com.lowagie.text.DocumentException
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

interface PdfService {
    @Throws(DocumentException::class, IOException::class)
    fun export(response: HttpServletResponse)
}