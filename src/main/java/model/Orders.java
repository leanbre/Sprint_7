package model;

public class Orders {
    // Основные поля заказа
    private String deliveryDate;
    private String address;
    private String metroStation;
    private int rentTime;
    // TODO: Понять/спросить, почему тут массив
    private String[] color;
    // Пользовательские данные
    private String phone;
    private String firstName;
    private String lastName;
    private String comment;

    public Orders(
            String deliveryDate,
            String address,
            String metroStation,
            int rentTime,
            String[] color,
            String phone,
            String firstName,
            String lastName,
            String comment
    ) {
        this.deliveryDate = deliveryDate;
        this.address = address;
        this.metroStation = metroStation;
        this.rentTime = rentTime;
        this.color = color;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comment = comment;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

