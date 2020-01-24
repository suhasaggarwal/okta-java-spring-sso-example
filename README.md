# Build a Single Sign-on Application in Java
 
This example app demonstrates how to use Okta and Spring Boot to implement single sign-on with a separate client application and resource server.

Please read [Build a Single Sign-on Application in Java](https://<need.a.link>) to see how this app was created.

#### ^-- TODO: needs a link --^

**Prerequisites:** 

**Java 11**: This project uses Java 11. If you don't have Java 11, you can install OpenJDK. Instructions are found on the [OpenJDK website](https://openjdk.java.net/install/).

**Yarn**: Yarn is a javascript package manager. You'll use it for the React UI application. To install Yarn, head to [their website for instructions](https://yarnpkg.com/lang/en/docs/install/#mac-stable).

**Okta Developer Account**: You'll be using Okta as an OAuth/OIDC provider to add JWT authentication and authorization to the application. Go to [their website](https://developer.okta.com/signup/) and sign up for one of their free developer accounts, if you haven't already.

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Start the Apps](#start-the-apps)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example application, run the following commands:

```bash
git clone https://<need.a.link> kotlin-react-app
cd kotlin-react-app
```

This will get a copy of the project installed locally. Before the projects apps will run, however, you need to create an OIDC application in Okta and configure the client and server to use it.

### Create an OIDC Application in Okta

You will need to [create an OIDC Application in Okta]() to get your values to perform authentication. 

Log in to your Okta Developer account (or [sign up](https://developer.okta.com/signup/) if you don’t have an account) and navigate to **Applications** > **Add Application**. Click **Single-Page App**, click **Next**, and give the app a name you’ll remember. Specify `http://localhost:3000/implicit/callback` as a **Login Redirect URI**. Specify `http://localhost:3000` as a **Base URI**. Click **Done**. 

#### Server Configuration

Set the `issuer` and copy the `clientId` into `src/main/resources/application.properties`. 

**NOTE:** The value of `{yourOktaUrl}` should be something like `dev-123456.com`. Make sure you don't include `-admin` in the value!

```properties
okta.oauth2.issuer=https://{yourOktaUrl}/oauth2/default  
okta.oauth2.clientId={yourClientId}
```

#### Client Configuration

The React client also needs to be configured with the Okta OAuth properties. Open the `src.App.js` file and look in the `App` component, toward the bottom of the module. You need to fill in your Client ID and Issuer URL in the properties of the Security component.

```jsx
class App extends Component {

    render() {

        return (
            <Router>
                <Security issuer='https://{yourOktaUrl}/oauth2/default'
                          clientId='{yourCliendId}'
                          redirectUri={window.location.origin + '/implicit/callback'}
                          pkce={true}
                >
                    <Route path='/implicit/callback' component={ImplicitCallback} />
                    <AuthWrapper />
                </Security>
            </Router>
        )
    }
}
```

## Start the Apps

To install all of its dependencies and start each app, follow the instructions below.

To run the server, run:
 
```bash
./gradlew bootRun
```

To run the client, cd into the `app` folder and run:
 
```bash
yarn && yarn start
```

## Links

This example uses the following open source libraries:

* [React](https://reactjs.org/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Security](https://spring.io/projects/spring-security)

## Help

Please post any questions as comments on the [blog post](https://need.a.link), or visit our [Okta Developer Forums](https://devforum.okta.com/). You can also email developers@okta.com if you'd like to create a support ticket.

#### ^-- TODO: needs a link --^

## License

Apache 2.0, see [LICENSE](LICENSE).
