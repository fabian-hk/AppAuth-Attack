# Description



# Requirements

1. Android Studio
2. Postman
3. Docker

# Running

1. Add c2id.local to your ``/etc/hosts`` file
    ```
    10.0.2.2	c2id.local
    ```
2. Run ``git init submodules``
3. Start the connect2id server in the ``c2id``
folder: ``./start.sh``
4. Import the Postman configuration into Postman
and create a client with the ``Create Client`` request.
5. Copy the ``client_id`` into the ``MainActivity.kt``
file of the ``AppAuth-Attack`` project and into the
``res/raw/auth_config.json`` file of the ``AppAuth-Android``
project
6. Compile and install the AppAuth-Android and
AppAuth-Attack app on your virtual device
7. Start the OpenID Connect flow in the AppAuth
app and choose at the redirect back to the app the 
AqqAuth app.

# License

This project is licensed under the MIT License
with the exception of the app icons which
are from the [AppAuth-Android](https://github.com/openid/AppAuth-Android)
project and thereby licensed under the Apache-2.0 License.
