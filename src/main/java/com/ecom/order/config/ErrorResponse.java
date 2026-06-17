package com.ecom.order.config;

public class ErrorResponse {

    private Object data;
    private ErrorDetail error;
    private Meta meta;

    public ErrorResponse() {}

    public ErrorResponse(Object data, ErrorDetail error, Meta meta) {
        this.data = data;
        this.error = error;
        this.meta = meta;
    }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public ErrorDetail getError() { return error; }
    public void setError(ErrorDetail error) { this.error = error; }

    public Meta getMeta() { return meta; }
    public void setMeta(Meta meta) { this.meta = meta; }

    public static class ErrorDetail {
        private String code;
        private String message;
        private Object details;

        public ErrorDetail() {}

        public ErrorDetail(String code, String message, Object details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Object getDetails() { return details; }
        public void setDetails(Object details) { this.details = details; }
    }

    public static class Meta {
        private String requestId;
        private String timestamp;

        public Meta() {}

        public Meta(String requestId, String timestamp) {
            this.requestId = requestId;
            this.timestamp = timestamp;
        }

        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }
}
