package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.bill.BillResponse
import com.belajar.api.kotlin.service.PdfService
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Color

@Service
@Slf4j
class PdfServiceImpl(
    val billResponseList: List<BillResponse>
) : PdfService {
    private val log = LoggerFactory.getLogger(JwtServiceImpl::class.java)

    private fun writeTabHeader(table: PdfPTable) {
        val cell = PdfPCell()
        cell.backgroundColor = Color.PINK
        cell.setPadding(5f)

        val font = FontFactory.getFont(FontFactory.HELVETICA)
        font.size = 12f
        font.color = Color.BLACK

        cell.phrase = Phrase("Bill ID", font)
        table.addCell(cell)

        cell.phrase = Phrase("Transaction Date", font)
        table.addCell(cell)

        cell.phrase = Phrase("Transaction Type", font)
        table.addCell(cell)

        cell.phrase = Phrase("Customer Name", font)
        table.addCell(cell)

        cell.phrase = Phrase("Table", font)
        table.addCell(cell)

        cell.phrase = Phrase("Payment ID", font)
        table.addCell(cell)

        cell.phrase = Phrase("Status", font)
        table.addCell(cell)
    }

    private fun writeTableData(table: PdfPTable) {
        for (bill in billResponseList) {
            table.addCell(bill.id)
            table.addCell(bill.transDate.toString())
            table.addCell(bill.transType)
            table.addCell(bill.customerName)
            table.addCell(bill.tableName)
            table.addCell(bill.payment.id)
            table.addCell(bill.payment.transactionStatus)
        }
    }

    @Throws(DocumentException::class)
    override fun export(response: HttpServletResponse) {
        try {
            Document(PageSize.A4.rotate()).use { document ->
                PdfWriter.getInstance(document, response.outputStream)
                document.open()
                val font =
                    FontFactory.getFont(FontFactory.HELVETICA, 14f, Font.HELVETICA)
                font.color = Color.PINK

                val billReports = Paragraph("Bill Reports", font)
                billReports.alignment = Paragraph.ALIGN_CENTER

                document.add(billReports)

                val table = PdfPTable(7)
                table.widthPercentage = 100f
                table.setWidths(floatArrayOf(2.0f, 3.5f, 3.5f, 3.0f, 2.0f, 2.0f, 1.5f))
                table.setSpacingBefore(10f)

                writeTabHeader(table)
                writeTableData(table)
                document.add(table)
            }
        } catch (e: Exception) {
            log.error("Error exporting PDF", e)
        }
    }
}