package testng;

import com.github.javafaker.Faker;
import devices.DeviceFarm;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AddContactPage;
import pages.HomePage;
import utility.DeviceFarmUtility;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.testng.Assert.*;

public class ContactManagerTestNG {

    HomePage homePage;
    AddContactPage addContactPage;
    String oreo;
    DesiredCapabilities capabilities;
    public static AppiumDriver<?> driver; //Bunun yerine WebDriver driver; 'da yazabilirdik. Cunku totalde hepsi (Android, ios vs vs) WebDriver'dan extends alıyor.
    Faker faker = new Faker();
    String simpleName = faker.name().firstName();
    String nameAsNumber = faker.numerify("######");
    String random = faker.bothify("???###?#?#");
    ArrayList fields = new ArrayList();
    WebDriverWait wait;


    public ContactManagerTestNG() {
        oreo = DeviceFarm.ANDROID_OREO.path;
    }

    @BeforeClass
    public void setUp() {
        try {
            capabilities = new DesiredCapabilities();
            capabilities = DeviceFarmUtility.pathToDesiredCapabilitites(this.oreo);
            capabilities.setCapability("app", new File("src/test/resources/apps/ContactManager.apk").getAbsolutePath());
            driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
            homePage = new HomePage();
            addContactPage = new AddContactPage();
        } catch (MalformedURLException e) {
            System.out.println("Please check URL. \n Error:" + e.getMessage());
        }
    }

    //Is the "Show Invisible Contacts(Only)" button active?
    @Test(priority = 0)
    public void checkInvisibleBtn() {
        assertTrue(homePage.getInvisibleBtn().isEnabled());
    }

    //Is AddContactPage's title "Add Contact"?
    @Test(priority = 1)
    public void checkAddContactPageTitle() {
        homePage.getAddContactBtn().click();
        try {
            wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/title")));
            assertEquals(addContactPage.getTitle().getText(), "Add Contact");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    //Can Name, Phone and Email fields be used?
    @Test(dependsOnMethods = "checkAddContactPageTitle", priority = 2)
    public void checkFieldsAreEnabled() {
        assertTrue(addContactPage.getContactNameField().isEnabled(), "Name field is not available.");
        assertTrue(addContactPage.getContactPhoneField().isEnabled(), "Phone field is not available.");
        assertTrue(addContactPage.getContactEmailField().isEnabled(), "Email field is not available.");
        assertTrue(addContactPage.getContactSaveBtn().isEnabled(), "Save button is not available.");
    }

    /*================================================================================================================================================
      THIS PART IS CHECKING FOR  LETTER CHARACTERS INPUT FOR NAME, PHONE AND EMAIL FIELDS.
      ==============================================================================================================================================*/

    //Does the Name field accept characters?
    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 2)
    public void checkNameWithCharacter() {
        addContactPage.getContactNameField().sendKeys(simpleName);
        assertEquals(addContactPage.getContactNameField().getText(), simpleName);
    }

    //Does the Phone field accept characters?
    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 3)
    public void checkPhoneWithCharacter() {
        addContactPage.getContactPhoneField().sendKeys(simpleName);
        assertEquals(addContactPage.getContactPhoneField().getText(), simpleName);
    }

    //Is DropBox's title "Select Label"?
    @Test(dependsOnMethods = "checkAddContactPageTitle", priority = 4)
    public void checkContactPhoneDropBoxTitle() {
        addContactPage.getContactPhoneDrop().click();
        try {
            wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/alertTitle")));
            assertEquals(addContactPage.getDropBoxTitle().getText(), "Select label");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    // Is the value of the Home index equal to "Home"?
    @Test(priority = 5)
    public void checkContactPhoneDropBox_Home() {
        assertEquals(addContactPage.getDropBoxHome().getText(), "Home");
        addContactPage.getDropBoxHome().click();
    }

    //Does the Email field accept characters?
    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 6)
    public void checkEmailWithCharacter() {
        addContactPage.getContactEmailField().sendKeys(simpleName + "@gmail.com");
        assertTrue(addContactPage.getContactEmailField().getText().contains("@"), "Wrong email adress.");
    }

    //Is DropBox's title "Select Label"?
    @Test(dependsOnMethods = "checkAddContactPageTitle", priority = 7)
    public void checkContactEmailDropBoxTitle() {
        addContactPage.getContactEmailDrop().click();
        try {
            wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/alertTitle")));
            assertEquals(addContactPage.getDropBoxTitle().getText(), "Select label");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    // Is the value of the Home index equal to "Home"?
    @Test(priority = 8)
    public void checkContactEmailDropBox_Home() {
        assertEquals(addContactPage.getDropBoxHome().getText(), "Home");
        addContactPage.getDropBoxHome().click();
    }

    //Are all fields filled?
    @Test(priority = 9)
    public void checkFieldsAreFilled_1() {
        //Get values from fields.
        try {
            fields.add(addContactPage.getContactNameField().getText());
            fields.add(addContactPage.getContactPhoneField().getText());
            fields.add(addContactPage.getContactEmailField().getText());
            assertFalse(fields.isEmpty(), "All fields full.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        //If fields are not empty, click save button.
        addContactPage.getContactSaveBtn().click();
    }

    /*================================================================================================================================================
      THIS PART IS CHECKING FOR SPECIAL CHARACTERS INPUT FOR NAME, PHONE AND EMAIL FIELDS.
      ==============================================================================================================================================*/

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 10)
    public void checkNameWithSpecialCharacter() {
        homePage.getAddContactBtn().click();
        addContactPage.getContactNameField().sendKeys("**??+");
        assertEquals(addContactPage.getContactNameField().getText(), "**??+");
    }

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 11)
    public void checkPhoneWithSpecialCharacter() {
        addContactPage.getContactPhoneField().sendKeys("**??+");
        assertEquals(addContactPage.getContactPhoneField().getText(), "**??+");
    }

    @Test(priority = 12)
    public void checkContactPhoneDropBox_Work() {
        addContactPage.getContactPhoneDrop().click();
        assertEquals(addContactPage.getDropBoxWork().getText(), "Work");
        addContactPage.getDropBoxWork().click();
    }

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 13)
    public void checkEmailWithSpecialCharacter() {
        addContactPage.getContactEmailField().sendKeys("**??+" + "@gmail.com");
        assertTrue(addContactPage.getContactEmailField().getText().contains("@"), "Wrong email adress.");
    }

    @Test(priority = 14)
    public void checkContactEmailDropBox_Work() {
        addContactPage.getContactEmailDrop().click();
        assertEquals(addContactPage.getDropBoxWork().getText(), "Work");
        addContactPage.getDropBoxWork().click();
    }

    @Test(priority = 15)
    public void checkFieldsAreFilled_2() {
        try {
            fields.add(addContactPage.getContactNameField().getText());
            fields.add(addContactPage.getContactPhoneField().getText());
            fields.add(addContactPage.getContactEmailField().getText());
            assertFalse(fields.isEmpty(), "All fields full.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        addContactPage.getContactSaveBtn().click();
    }

    /*================================================================================================================================================
      THIS PART IS CHECKING FOR NUMBERS INPUT FOR NAME, PHONE AND EMAIL FIELDS.
      ==============================================================================================================================================*/

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 16)
    public void checkNameWithNumber() {
        homePage.getAddContactBtn().click();
        addContactPage.getContactNameField().sendKeys(nameAsNumber);
        assertEquals(addContactPage.getContactNameField().getText(), nameAsNumber);
    }

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 17)
    public void checkPhoneWitNumber() {
        addContactPage.getContactPhoneField().sendKeys(nameAsNumber);
        assertEquals(addContactPage.getContactPhoneField().getText(), nameAsNumber);
    }

    @Test(priority = 18)
    public void checkContactPhoneDropBox_Mobile() {
        addContactPage.getContactPhoneDrop().click();
        assertEquals(addContactPage.getDropBoxMobile().getText(), "Mobile");
        addContactPage.getDropBoxMobile().click();
    }

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 19)
    public void checkEmailWitNumber() {
        addContactPage.getContactEmailField().sendKeys(nameAsNumber + "@gmail.com");
        assertTrue(addContactPage.getContactEmailField().getText().contains("@"), "Wrong email adress.");
    }

    @Test(priority = 20)
    public void checkContactEmailDropBox_Mobile() {
        addContactPage.getContactEmailDrop().click();
        assertEquals(addContactPage.getDropBoxMobile().getText(), "Mobile");
        addContactPage.getDropBoxMobile().click();
    }

    @Test(priority = 21)
    public void checkFieldsAreFilled_3() {
        try {
            fields.add(addContactPage.getContactNameField().getText());
            fields.add(addContactPage.getContactPhoneField().getText());
            fields.add(addContactPage.getContactEmailField().getText());
            assertFalse(fields.isEmpty(), "All fields full.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        addContactPage.getContactSaveBtn().click();
    }

    /*================================================================================================================================================
      THIS PART IS CHECKING FOR RANDOM CHARACTERS AND NUMBERS INPUT FOR NAME, PHONE AND EMAIL FIELDS.
      ==============================================================================================================================================*/

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 22)
    public void checkNameAsRandom() {
        homePage.getAddContactBtn().click();
        addContactPage.getContactNameField().sendKeys(random);
        assertEquals(addContactPage.getContactNameField().getText(), random);
    }

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 23)
    public void checkPhoneAsRandom() {
        addContactPage.getContactPhoneField().sendKeys(random);
        assertEquals(addContactPage.getContactPhoneField().getText(), random);
    }

    @Test(priority = 24)
    public void checkContactPhoneDropBox_Other() {
        addContactPage.getContactPhoneDrop().click();
        assertEquals(addContactPage.getDropBoxOther().getText(), "Other");
        addContactPage.getDropBoxOther().click();
    }

    @Test(dependsOnMethods = "checkFieldsAreEnabled", priority = 25)
    public void checkEmailRandom() {
        addContactPage.getContactEmailField().sendKeys(random + "@gmail.com");
        assertTrue(addContactPage.getContactEmailField().getText().contains("@"), "Wrong email adress.");
    }

    @Test(priority = 26)
    public void checkContactEmailDropBox_Other() {
        addContactPage.getContactEmailDrop().click();
        assertEquals(addContactPage.getDropBoxOther().getText(), "Other");
        addContactPage.getDropBoxOther().click();
    }

    @Test(priority = 27)
    public void checkFieldsAreFilled_4() {
        try {
            fields.add(addContactPage.getContactNameField().getText());
            fields.add(addContactPage.getContactPhoneField().getText());
            fields.add(addContactPage.getContactEmailField().getText());
            assertFalse(fields.isEmpty(), "All fields full.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        addContactPage.getContactSaveBtn().click();
    }

}

