package pl.edu.pb.carassistant.User;

import android.net.Uri;

public class User {

    private String userId, userName, carBrand, carModel, carYear, carMileage, carRegistrationNumber;
    private Uri userPhoto;


    public User(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCarBrand() {
        return carBrand;
    }

    public void setUserCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getUserCarModel() {
        return carModel;
    }

    public void setUserCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getUserCarYear() {
        return carYear;
    }

    public void setUserCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getUserCarMileage() {
        return carMileage;
    }

    public void setUserCarMileage(String carMileage) {
        this.carMileage = carMileage;
    }

    public String getUserCarRegistrationNumber() {
        return carRegistrationNumber;
    }

    public void setUserCarRegistrationNumber(String carRegistrationNumber) {
        this.carRegistrationNumber = carRegistrationNumber;
    }

    public Uri getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
    }
}
