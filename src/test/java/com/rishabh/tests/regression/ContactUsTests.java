package com.rishabh.tests.regression;

import com.rishabh.framework.utils.RandomDataUtil;
import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

@Epic("Ecommerce Automation")
@Feature("Contact Us")
public class ContactUsTests extends BaseTest {

    private static final String SAMPLE_FILE = new File("src/test/resources/testdata/sample_upload.txt").getAbsolutePath();

    @Test(groups = {"smoke", "regression"}, description = "TC06 - Submitting a fully filled contact form shows a success banner")
    public void contactUs_submitValidForm_showsSuccessMessage() {
        goToHomePage().header().goToContactUs();
        pages.getContactUsPage().fillForm(RandomDataUtil.fullName(), RandomDataUtil.uniqueEmail(),
                "Query about order status", "Just checking in on my recent order, thanks.");
        pages.getContactUsPage().submit();

        Assert.assertTrue(pages.getContactUsPage().isSuccessMessageVisible());
        Assert.assertTrue(pages.getContactUsPage().getSuccessMessageText().toLowerCase().contains("success"));
    }

    @Test(groups = "regression", description = "Contact form blocks submission with the name field empty")
    public void contactUs_submitWithoutName_blocked() {
        goToHomePage().header().goToContactUs();
        pages.getContactUsPage().fillForm("", RandomDataUtil.uniqueEmail(), "Subject", "Some message body");
        pages.getContactUsPage().submit();

        Assert.assertFalse(pages.getContactUsPage().isSuccessMessageVisible());
    }

    @Test(groups = "regression", description = "Contact form blocks submission with the email field empty")
    public void contactUs_submitWithoutEmail_blocked() {
        goToHomePage().header().goToContactUs();
        pages.getContactUsPage().fillForm(RandomDataUtil.fullName(), "", "Subject", "Some message body");
        pages.getContactUsPage().submit();

        Assert.assertFalse(pages.getContactUsPage().isSuccessMessageVisible());
    }

    @Test(groups = "regression", description = "Contact form blocks submission with the message field empty")
    public void contactUs_submitWithoutMessage_blocked() {
        goToHomePage().header().goToContactUs();
        pages.getContactUsPage().fillForm(RandomDataUtil.fullName(), RandomDataUtil.uniqueEmail(), "Subject", "");
        pages.getContactUsPage().submit();

        Assert.assertFalse(pages.getContactUsPage().isSuccessMessageVisible());
    }

    @Test(groups = {"smoke", "regression"}, description = "Contact form accepts a file attachment and still submits fine")
    public void contactUs_withFileAttachment_submitsSuccessfully() {
        goToHomePage().header().goToContactUs();
        pages.getContactUsPage().fillForm(RandomDataUtil.fullName(), RandomDataUtil.uniqueEmail(),
                "Attaching a screenshot", "Please see the attached file for reference.");
        pages.getContactUsPage().attachFile(SAMPLE_FILE);
        pages.getContactUsPage().submit();

        Assert.assertTrue(pages.getContactUsPage().isSuccessMessageVisible());
    }

    @Test(groups = "regression", description = "'Home' button after a successful submission returns to the home page")
    public void contactUs_afterSuccess_homeButtonReturnsHome() {
        goToHomePage().header().goToContactUs();
        pages.getContactUsPage().fillForm(RandomDataUtil.fullName(), RandomDataUtil.uniqueEmail(),
                "General feedback", "Loving the site so far!");
        pages.getContactUsPage().submit();

        pages.getContactUsPage().goHome();
        Assert.assertTrue(pages.getHomePage().isLoaded());
    }
}
