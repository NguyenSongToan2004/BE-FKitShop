package com.group4.FKitShop;

public class ResponseObject {
    private int status;
    private String message; // Thêm trường message
    private Object data;

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private String message; // Thêm trường message
        private Object data;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) { // Thêm phương thức builder cho message
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseObject build() {
            ResponseObject response = new ResponseObject();
            response.status = this.status;
            response.message = this.message; // Gán message
            response.data = this.data;
            return response;
        }
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() { // Thêm getter cho message
        return message;
    }

    public Object getData() {
        return data;
    }
}


