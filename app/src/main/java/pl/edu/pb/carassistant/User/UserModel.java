package pl.edu.pb.carassistant.User;

import android.net.Uri;

public class UserModel {

    String userId, userName, carBrand, carModel, carYear, carMileage, carRegistrationNumber, carAvgConsumption;
    Uri userPhoto;

    public UserModel() {
    }

    public UserModel(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getUserCarAvgConsumption() {
        return carAvgConsumption;
    }

    public void setUserCarAvgConsumption(String carAvgConsumption) {
        this.carAvgConsumption = carAvgConsumption;
    }

    public Uri getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
    }
}
