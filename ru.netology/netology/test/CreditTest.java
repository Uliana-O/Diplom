package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.FormPage;
import ru.netology.page.PaymentPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
    FormPage formPage;
    FormPage.MessageWrong mess = new FormPage.MessageWrong();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void paymentTur() {
        var payPage = new PaymentPage();
        formPage = payPage.validPaymentCredit();
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.cleanDataBase();
    }

    @Test
    @DisplayName("32.Credit with a approved card")
    void creditApprovedCard() {
        var card = DataHelper.getValidCardInfo();
        formPage.setPaymentCardInfo(card);

        formPage.messageSuccessfulNotification();
        assertEquals("APPROVED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("33.Credit with a declined card")
    void creditDeclinedCard() {
        var card = DataHelper.cardNumberValidate(DataHelper.getInValidCardNumber());
        formPage.setPaymentCardInfo(card);

        formPage.messageErrorNotification();
        assertEquals("DECLINED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("34.Credit.Empty form")
    void creditEmptyForm() {
        var card = DataHelper.getEmptyCardInfo();
        formPage.setPaymentCardInfo(card);

        formPage.messageWrong(0, mess.getEmptyField());
        formPage.messageWrong(1, mess.getEmptyField());
        formPage.messageWrong(2, mess.getEmptyField());
        formPage.messageWrong(3, mess.getEmptyField());
        formPage.messageWrong(4, mess.getEmptyField());
    }

    @Test
    @DisplayName("35.Credit.Empty card number")
    void creditEmptyCardNumber() {
        var card = DataHelper.cardNumberValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getEmptyField());
    }

    @Test
    @DisplayName("36.Credit.Empty month")
    void creditEmptyMonth() {
        var card = DataHelper.monthValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getEmptyField());
    }

    @Test
    @DisplayName("37.Credit.Empty year")
    void creditEmptyYear() {
        var card = DataHelper.yearValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 3, 4);
        formPage.messageWrong(2, mess.getEmptyField());
    }

    @Test
    @DisplayName("38.Credit.Empty cardholder name")
    void creditEmptyCardholderName() {
        var card = DataHelper.cardholderNameValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 2, 4);
        formPage.messageWrong(3, mess.getEmptyField());
    }

    @Test
    @DisplayName("39.Credit.Empty cvc-code")
    void creditEmptyCVC() {
        var card = DataHelper.cvcCodeValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 2, 3);
        formPage.messageWrong(4, mess.getEmptyField());
    }

    @Test
    @DisplayName("40.Credit.Card number validation.15 numbers")
    void creditCardNumber15numbers() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateNumbers(15));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
    }

    @Test
    @DisplayName("41.Credit.Card number validation.17 numbers")
    void creditCardNumber17numbers() {
        var number = DataHelper.generateNumbers(17);
        var card = DataHelper.cardNumberValidate(number);
        formPage.setPaymentCardInfo(card);


        formPage.messageErrorNotification();
        formPage.valueAttribute(0, number.substring(0, 16));
    }

    @Test
    @DisplayName("42.Credit.Card number validation.Rus letter")
    void creditCardNumberRusLetter() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateRusLetter());
        formPage.setPaymentCardInfo(card);


        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
        formPage.valueAttribute(0, "");
    }


    @Test
    @DisplayName("43.Credit.Card number validation.Special symbols")
    void creditCardNumberSpecialSymbols() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateSpecialSymbols());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
        formPage.valueAttribute(0, "");
    }

    @Test
    @DisplayName("44.Credit.Card number validation.Zero")
    void creditCardNumberZero() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateZero(16));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
    }

    @Test
    @DisplayName("45.Credit.Not exist month")
    void creditNotExistMonth() {
        var card = DataHelper.monthValidate(DataHelper.generateNumbersBetween(13, 99));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getWrongValidity());
    }

    @Test
    @DisplayName("46.Credit.Month.1 number")
    void creditMonth1number() {
        var card = DataHelper.monthValidate(DataHelper.generateNumbers(1));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getWrongFormat());
    }

    @Test
    @DisplayName("47.Credit.Month.Rus letter) " +
            void creditMonthRusLetter(){
            var card=DataHelper.monthValidate(DataHelper.generateRusLetter());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 2, 3, 4);
            formPage.messageWrong(1,mess.getWrongFormat());
            formPage.valueAttribute(1,"");

    @Test
    @DisplayName("55.Credit.CardholderName.Partial name")
    void creditPartialCardholderName() {
            var card = DataHelper.cardholderNameValidate(DataHelper.generateLatin().substring(0, 1));
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 4);
            formPage.messageWrong(3, mess.getWrongFormat());
    }


    @Test
    @DisplayName("48.Credit.Month Special symbols")
    void creditMonthChar() {
        var card = DataHelper.monthValidate(DataHelper.generateChar());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getWrongFormat());
        formPage.valueAttribute(1, "");
    }


    @Test
    @DisplayName("49.Credit.Expired month")
    void creditExpiredMonth() {

        var monthInt = Byte.parseByte(DataHelper.generateValidMonth()) - 1;
        String monthStr = String.valueOf(monthInt);

        var card = DataHelper.monthValidate(monthStr);
        formPage.setPaymentCardInfo(card);


        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getExpiredValidity());
    }

    @Test
    @DisplayName("50.Credit.Not exist year")
    void creditNotExistYear() {
        var yearInt = Byte.parseByte(DataHelper.generateValidYear()) + 11;
        String yearStr = String.valueOf(yearInt);

        var card = DataHelper.yearValidate(yearStr);
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 3, 4);
        formPage.messageWrong(2, mess.getWrongValidity());
    }

    @Test
    @DisplayName("51.Credit.Year.1 number")
    void creditYear1number() {
        var card = DataHelper.yearValidate(DataHelper.generateNumbers(1));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 3, 4);
        formPage.messageWrong(2, mess.getWrongFormat());
    }

    @Test
    @DisplayName("52.Credit.Year.Rus letter")
    void creditYearRusLetter() {
        var card = DataHelper.yearValidate(DataHelper.generateRusLetter());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 3, 4);
        formPage.messageWrong(2, mess.getWrongFormat());
        formPage.valueAttribute(2, "");
    }

    @Test
    @DisplayName("53.Credit.Year.Special symbols")
    void creditYearSpecialSymbols() {
        var card = DataHelper.yearValidate(DataHelper.generateSpecialSymbols());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 3, 4);
        formPage.messageWrong(2, mess.getWrongFormat());
        formPage.valueAttribute(2, "");
    }

    @Test
    @DisplayName("54.Credit.Expired year")
    void creditExpiredYear() {
        var yearInt = Byte.parseByte(DataHelper.generateValidYear()) - 1;
        String yearStr = String.valueOf(yearInt);

        var card = DataHelper.yearValidate(yearStr);
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 3, 4);
        formPage.messageWrong(2, mess.getExpiredValidity());
    }


    @Test
    @DisplayName("56.Credit.CardholderName.Rus letter)
        void creditCardholderNameRusLetter(){
        var card = DataHelper.cardholderNameValidate(DataHelper.generateRusLetter());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 2, 4);
        formPage.messageWrong(3,mess.getWrongFormat());
        formPage.valueAttribute(3,"");
    }

    @Test
    @DisplayName("57.Credit.CardholderName.Numbers")
        void creditCardholderNameNumbers(){
        var card=DataHelper.cardholderNameValidate(DataHelper.generateNumbers(2));
        formPage.setPaymentCardInfo(card);

            formPage.emptySub(0,1,2,4);
            formPage.messageWrong(3,mess.getWrongFormat());
            formPage.valueAttribute(3,"");
    }


    @Test
    @DisplayName("58.Credit.CardholderName.Special symbols")
        void creditCardholderNameSpecialSymbols(){
                var card=DataHelper.cardholderNameValidate(DataHelper.generateSpecialSymbols());
                formPage.setPaymentCardInfo(card);

                formPage.emptySub(0,1,2,4);
                formPage.messageWrong(3,mess.getWrongFormat());
                formPage.valueAttribute(3,"");
    }


    @Test
    @DisplayName("59.Credit.CVC.2 number")
        void creditCVC2number(){
        var card=DataHelper.cvcCodeValidate(DataHelper.generateNumbers(2));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0,1,2,3);
        formPage.messageWrong(4,mess.getWrongFormat());
    }


    @Test
    @DisplayName("60.Credit.CVC.Zero")
        void creditCVCZero(){
        var card=DataHelper.cvcCodeValidate(DataHelper.generateZero(3));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0,1,2,3);
        formPage.messageWrong(4,mess.getWrongFormat());
    }

    @Test
    @DisplayName("61.Credit.CVC.Rus letter")
        void creditCVCRusLetter(){
        var card=DataHelper.cvcCodeValidate(DataHelper.generateRusLetter());
        formPage.setPaymentCardInfo(card);

                formPage.emptySub(0,1,2,3);
                formPage.messageWrong(4,mess.getWrongFormat());
                formPage.valueAttribute(4,"");
    }

    @Test
    @DisplayName("62.Credit.CVC.Special symbol")
        void creditCVCSpecialSymbol() {
        var card=DataHelper.cvcCodeValidate(DataHelper.generateSpecialSymbol());
        formPage.setPaymentCardInfo(card);

                formPage.emptySub(0,1,2,3);
                formPage.messageWrong(4,mess.getWrongFormat());
                formPage.valueAttribute(4,"");
         }
    }

}






