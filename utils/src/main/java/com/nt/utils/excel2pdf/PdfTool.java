package com.nt.utils.excel2pdf;

import com.itextpdf.text.Document;

import java.io.OutputStream;

public class PdfTool {
    //
    protected Document document;
    //
    protected OutputStream os;

    public Document getDocument() {
        if (document == null) {
            document = new Document();
        }
        return document;
    }
}
