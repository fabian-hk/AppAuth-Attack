# Description
This repository shows a code stealing attack
on the AppAuth-Android library that is possible
if a custom scheme is used as the redirect URL.
The attack leverages the fact that users have to
choose between apps if a custom scheme is registered
by multiple apps. If the user chooses the malicious app
and the authorization flow does not use PKCE, the 
adversary can
exchange the authorization code for an ID Token
at the token endpoint. The procedure of the attack
can be seen in the following sequence diagram:

<img src="https://www.plantuml.com/plantuml/png/XO_HIWCn44NVynL3NxHWVs0H8LIBuiDAenymn5aIMdPYCal1htV2sbq2Wc-PUy_atf4eP-h0Yg0_mU7C5sCIPea-9Xm5FWhbHkXTCswUUQGYO5FQ01O8sDhpnvHloe9WYrXdJ4s1tODkT-14JR2J1Cxn6oNoQ09MrxgkrU58NrDDlqHL9u6tr_sJwHtSx_w4I-An4Dnw_gJj7AfahsG_33fWIk03HqzBeshk0tCXBRxUecJDKG3nlnnSoh_pYTRIg_rRzsfVruyqP2fXrk_WvN5-RNy00">

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
