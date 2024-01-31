package fr.newstaz.istore.response;

public record StoreResponse(boolean success, String message) {

    public record CreateStoreResponse(boolean success, String message) {

    }

    public record addEmployeeResponse(boolean success, String message) {

    }
}
