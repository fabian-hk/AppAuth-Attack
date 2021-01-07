# Description
This repository shows a code stealing attack
by the example of the AppAuth-Android library. The attack is possible
if a custom scheme is used as the redirect URL and if no PKCE is used.
The attack leverages the fact that users have to
choose between apps if a custom scheme is registered
by multiple apps. If the user chooses the malicious app the adversary can
exchange the authorization code for an ID Token
at the token endpoint. The procedure of the attack
can be seen in the following sequence diagram:

<img src="https://www.plantuml.com/plantuml/png/XP51ImCn48Nlyok6NhJIGlLOYhGKHV2mukgv3DafEtIRn6IMWh_U2SN0Wk1bITxxPjvRiirAISVp0FaaFbAYCtPHy66rWkneu79QtTtR1q151rxZJfNS2P-7S04Wfo7XP-O4DgtY999NRDh9TVOIF6V5NOmpf8mzJUthIautkI8qHNOjG4d1vQDDhx6rtgQEseSatwGIFFHahf7hgOxl_3MMz8kOEYg-lUwVe6bni_mJpZ7up7ZhGiVpQwYHZP7DyMWNYX5T7yHnRLJCVHz2PYjU_wMG9SS0elzog9R_22UkfSqUXBqUf5jW9JVB2VcxAV4nSUuFMlG5IdSzpM7BlhD7_W40">

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
AppAuth-Attack app on your virtual device.
7. Start the OpenID Connect flow in the AppAuth
app and choose at the redirect back to the app the 
AqqAuth app.
