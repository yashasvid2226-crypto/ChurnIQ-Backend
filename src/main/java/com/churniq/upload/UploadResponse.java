
package com.churniq.upload;

public class UploadResponse {

    private int totalRows;
    private int imported;
    private int duplicatesSkipped;
    private int invalidRows;
    private String status;

    public UploadResponse(int totalRows, int imported,
                          int duplicatesSkipped, int invalidRows,
                          String status) {
        this.totalRows = totalRows;
        this.imported = imported;
        this.duplicatesSkipped = duplicatesSkipped;
        this.invalidRows = invalidRows;
        this.status = status;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getImported() {
        return imported;
    }

    public int getDuplicatesSkipped() {
        return duplicatesSkipped;
    }

    public int getInvalidRows() {
        return invalidRows;
    }

    public String getStatus() {
        return status;
    }
}
