package ru.netology.test;



import com.codeborne.selenide.logvents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.FormPage;
import ru.netology.page.PaymentPage;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentTest {
    FormPage formPage;
    FormPage.MessageWeong mess = new FormPage.MessageWrong();

    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addLi
    }

    @AfterAll
    static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void paymentTur() {
        var payPage = new PaymentPage();
        formPage = payPage.validPaymentCard();

    }
    @AfterEach
    void cleanDB() {

        SQLHelper.cleanDataBase();
    }

    @Test
    @DisplayName("1.Payment with approved card")
    void paymentApprovedCard() {
        var card = DataHelper.getValidCardInfo();
        formPage.setPaymentCardInfo(card);

        formPage.messageSuccessfulNotification();
        assertEquals("APPROVED", SQLHelper.getStatusPayment());
    }

    @Test
    @DisplayName("2.Payment with declined card")
    void paymentDeclinedCard() {
        var card = DataHelper.cardNumberValidate(DataHelper.getInValidCardNumber());
        formPage.setPaymentCardInfo(card);

        formPage.messageErrorNotification();
        assertEquals("DECLINED", SQLHelper.getStatusPayment());
    }

    @Test
    @DisplayName("3.Payment.Empty form")
    void paymentEmptyForm() {
        var card = DataHelper.getEmptyCardInfo();
        formPage.setPaymentCardInfo(card);

        formPage.messageWrong(0, mess.getEmptyField());
        formPage.messageWrong(1, mess.getEmptyField());
        formPage.messageWrong(2, mess.getEmptyField());
        formPage.messageWrong(3, mess.getEmptyField());
        formPage.messageWrong(4, mess.getEmptyField());
    }

    @Test
    @DisplayName("4.Payment.Empty card number")
    void paymentEmptyCardNumber() {
        var card = DataHelper.cardNumberValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getEmptyField());
    }

    @Test
    @DisplayName("5.Payment.Empty month")
    void paymentEmptyMonth() {
        var card = DataHelper.monthValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getEmptyField());
    }

    @Test
    @DisplayName("6.Payment.Empty year")
    void paymentEmptyYear() {
        var card = DataHelper.yearValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 3, 4);
        formPage.messageWrong(2, mess.getEmptyField());
    }

    @Test
    @DisplayName("7.Payment.Empty cardholder name")
    void paymentEmptyCardholderName() {
        var card = DataHelper.cardholderNameValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 2, 4);
        formPage.messageWrong(3, mess.getEmptyField());
    }

    @Test
    @DisplayName("8.Payment.Empty cvc-code")
    void paymentEmptyCVC() {
        var card = DataHelper.cvcCodeValidate("");
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 1, 2, 3);
        formPage.messageWrong(4, mess.getEmptyField());
    }

    @Test
    @DisplayName("9.Payment.Card number validation.15 numbers")
    void paymentCardNumber15numbers() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateNumbers(15));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
    }

    @Test
    @DisplayName("10.Payment.Card number validation.17 numbers")
    void paymentCardNumber17numbers() {
        var number = DataHelper.generateNumbers(17);
        var card = DataHelper.cardNumberValidate(number);
        formPage.setPaymentCardInfo(card);

        formPage.messageErrorNotification();
        formPage.valueAttribute(0, number.substring(0, 16));
    }

    @Test
    @DisplayName("11.Payment.Card number validation. Rus letter")
    void paymentCardNumberRusLetter() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateRusLetter());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
        formPage.valueAttribute(0, "");
    }


    @Test
    @DisplayName("12.Payment.Card number validation.Special symbols")
    void paymentCardNumberSpecialSymbols() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateSpecialSymbols());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
        formPage.valueAttribute(0, "");
    }

    @Test
    @DisplayName("13.Payment.Card number validation.Zero")
    void paymentCardNumberZero() {
        var card = DataHelper.cardNumberValidate(DataHelper.generateZero(16));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(1, 2, 3, 4);
        formPage.messageWrong(0, mess.getWrongFormat());
    }


    @Test
    @DisplayName("14.Payment.Not exist month")
    void paymentNotExistMonth() {
        var card = DataHelper.monthValidate(DataHelper.generateNumbersBetween(13, 99));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getWrongValidity());
    }

    @Test
    @DisplayName("15.Payment.Month.1 number")
    void paymentMonth1number() {
        var card = DataHelper.monthValidate(DataHelper.generateNumbers(1));
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getWrongFormat());
    }

    @Test
    @DisplayName("16.Payment.Month.Rus letter")
    void paymentMonthRusLetter() {
        var card = DataHelper.monthValidate(DataHelper.generateRusLetter());
        formPage.setPaymentCardInfo(card);

        formPage.emptySub(0, 2, 3, 4);
        formPage.messageWrong(1, mess.getWrongFormat());
        formPage.valueAttribute(1, "");


        @Test
        @DisplayName("17.Payment.Month.Special symbols")
        void paymentMonthSpecialSymbols () {
            var card = DataHelper.monthValidate(DataHelper.generateSpecialSymbols());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 2, 3, 4);
            formPage.messageWrong(1, mess.getWrongFormat());
            formPage.valueAttribute(1, "");
        }


        @Test
        @DisplayName("18.Payment.Expired month")
        void paymentExpiredMonth () {

            var monthInt = Byte.parseByte(DataHelper.generateValidMonth()) - 1;
            String monthStr = String.valueOf(monthInt);

            var card = DataHelper.monthValidate(monthStr);
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 2, 3, 4);
            formPage.messageWrong(1, mess.getExpiredValidity());
        }


        @Test
        @DisplayName("19.Payment.Not exist year")
        void paymentNotExistYear () {
            var yearInt = Byte.parseByte(DataHelper.generateValidYear()) + 11;
            String yearStr = String.valueOf(yearInt);

            var card = DataHelper.yearValidate(yearStr);
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 3, 4);
            formPage.messageWrong(2, mess.getWrongValidity());
        }


        @Test
        @DisplayName("20.Payment.Year.1 number")
        void paymentYear1number () {
            var card = DataHelper.yearValidate(DataHelper.generateNumbers(1));
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 3, 4);
            formPage.messageWrong(2, mess.getWrongFormat());
        }


        @Test
        @DisplayName("21.Payment.Year.Rus letter")
        void paymentYearRusLetter () {
            var card = DataHelper.yearValidate(DataHelper.generateRusLetter());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 3, 4);
            formPage.messageWrong(2, mess.getWrongFormat());
            formPage.valueAttribute(2, "");
        }


        @Test
        @DisplayName("22.Payment.Year.Special symbols")
        void paymentYearSpecialSymbols () {
            var card = DataHelper.yearValidate(DataHelper.generateSpecialSymbols());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 3, 4);
            formPage.messageWrong(2, mess.getWrongFormat());
            formPage.valueAttribute(2, "");
        }

        @Test
        @DisplayName("23.Payment.Expired year")
        void paymentExpiredYear () {

            var yearInt = Byte.parseByte(DataHelper.generateValidYear()) - 1;
            String yearStr = String.valueOf(yearInt);

            var card = DataHelper.yearValidate(yearStr);
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 3, 4);
            formPage.messageWrong(2, mess.getExpiredValidity());
        }


        @Test
        @DisplayName("24.Payment.CardholderName.Partial name")
        void paymentPartialCardholderName () {
            var card = DataHelper.cardholderNameValidate(DataHelper.generateLatin().substring(0, 1));
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 4);
            formPage.messageWrong(3, mess.getWrongFormat());
        }
        @Test
        @DisplayName("25.Payment.CardholderName.Rus letter")
        void paymentCardholderNameRusLetter () {
            var card = DataHelper.cardholderNameValidate(DataHelper.generateRusLetter());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 4);
            formPage.messageWrong(3, mess.getWrongFormat());
            formPage.valueAttribute(3, "");
        }

        @Test
        @DisplayName("26.Payment.CardholderName.Numbers")
        void paymentCardholderNameNumbers () {
            var card = DataHelper.cardholderNameValidate(DataHelper.generateNumbers(2));
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 4);
            formPage.messageWrong(3, mess.getWrongFormat());
            formPage.valueAttribute(3, "");
        }


        @Test
        @DisplayName("27.Payment.CardholderName.Special symbols")
        void paymentCardholderNameSpecialSymbols () {
            var card = DataHelper.cardholderNameValidate(DataHelper.generateSpecialSymbols());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 4);
            formPage.messageWrong(3, mess.getWrongFormat());
            formPage.valueAttribute(3, "");
        }


        @Test
        @DisplayName("28.Payment.CVC.2 number")
        void paymentCVC2number () {
            var card = DataHelper.cvcCodeValidate(DataHelper.generateNumbers(2));
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 3);
            formPage.messageWrong(4, mess.getWrongFormat());
        }

        @Test
        @DisplayName("29.Payment.CVC.Zero")
        void paymentCVCZero () {
            var card = DataHelper.cvcCodeValidate(DataHelper.generateZero(3));
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 3);
            formPage.messageWrong(4, mess.getWrongFormat());
        }

        @Test
        @DisplayName("30.Payment.CVC.Rus letter")
        void paymentCVCRusLetter () {
            var card = DataHelper.cvcCodeValidate(DataHelper.generateRusLetter());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 3);
            formPage.messageWrong(4, mess.getWrongFormat());
            formPage.valueAttribute(4, "");
        }

        @Test
        @DisplayName("31.Payment.CVC.Special symbols")
        void paymentCVCSpecialSymbols () {
            var card = DataHelper.cvcCodeValidate(DataHelper.DataHelper.generateSpecialSymbols());
            formPage.setPaymentCardInfo(card);

            formPage.emptySub(0, 1, 2, 3);
            formPage.messageWrong(4, mess.getWrongFormat());
            formPage.valueAttribute(4, "");
        }


    }
}
}






