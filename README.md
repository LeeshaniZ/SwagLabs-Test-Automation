# SwagLabs UI Test Automation

A Java-based browser automation project that exercises a complete shopping and checkout journey on the public SauceDemo sample application.

## Technology

- Java 11+
- Selenium WebDriver 4
- TestNG
- Maven
- Google Chrome

## Automated scenario

The end-to-end test verifies that a user can:

1. Sign in to the demo application.
2. Open the products page and add a product to the cart.
3. Confirm the cart contents.
4. Enter checkout information.
5. Review the order summary and verify that subtotal plus tax equals the displayed total.
6. Complete the order and confirm the success message.

The test uses TestNG assertions, Selenium explicit waits, environment-based settings, and a screenshot saved under `target/screenshots/`.

## Prerequisites

- JDK 11 or later
- Maven 3.8+
- Google Chrome
- Internet access to reach SauceDemo and download Maven dependencies

Selenium Manager (included with Selenium 4) manages the compatible browser driver automatically in supported environments; a manually configured ChromeDriver path is normally unnecessary.

## Run the test

From the repository root:

```bash
mvn test
```

Run in headless mode:

Windows PowerShell:

```powershell
$env:HEADLESS = "true"
mvn test
```

macOS / Linux:

```bash
HEADLESS=true mvn test
```

## Optional environment variables

| Variable | Default | Purpose |
|---|---|---|
| `SWAG_LABS_URL` | `https://www.saucedemo.com/` | Demo application URL |
| `SWAG_LABS_USERNAME` | `standard_user` | SauceDemo sample account |
| `SWAG_LABS_PASSWORD` | `secret_sauce` | SauceDemo sample password |
| `HEADLESS` | `false` | Run Chrome without a visible browser window |

The default credentials are public sample credentials provided by SauceDemo, not personal account credentials.

## Project structure

```text
SwagLabs-Test-Automation/
├── src/
│   └── test/
│       └── java/
│           └── TestNG/
│               └── SwagLabsTest.java
├── pom.xml
└── README.md
```

## Test output

- Maven test reports: `target/surefire-reports/`
- Checkout screenshot: `target/screenshots/checkout-complete.png`

## Scope and limitations

This is a UI automation learning project for a third-party demo website. It does not test a production payment gateway or a real retail application. Browser execution depends on the demo site being reachable and its UI remaining compatible with the test selectors.
