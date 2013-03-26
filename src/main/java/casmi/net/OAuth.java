/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.net;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import casmi.exception.NetException;

/**
 * OAuth class.
 *
 * @author T. Takeuchi
 *
 */
public class OAuth {

    private oauth.signpost.OAuthConsumer consumer = null;
    private oauth.signpost.OAuthProvider provider = null;

    public OAuth() {}

    public void setConsumer(String consumerKey, String consumerSecret) {

        consumer = new oauth.signpost.basic.DefaultOAuthConsumer(consumerKey, consumerSecret);
    }

    public void setProvider(String requestTokenEndpointUrl, String accessTokenEndpointUrl,

        String authorizationWebsiteUrl) {
        provider = new oauth.signpost.basic.DefaultOAuthProvider(
            requestTokenEndpointUrl,
            accessTokenEndpointUrl,
            authorizationWebsiteUrl);
    }

    public String retrieveRequestToken() throws NetException {

        try {
            return provider.retrieveRequestToken(consumer, oauth.signpost.OAuth.OUT_OF_BAND);
        } catch (OAuthMessageSignerException e) {
            throw new NetException(e);
        } catch (OAuthNotAuthorizedException e) {
            throw new NetException(e);
        } catch (OAuthExpectationFailedException e) {
            throw new NetException(e);
        } catch (OAuthCommunicationException e) {
            throw new NetException(e);
        }
    }

    public void retrieveAccessToken(String pin) throws NetException {

        try {
            provider.retrieveAccessToken(consumer, pin);
        } catch (OAuthMessageSignerException e) {
            throw new NetException(e);
        } catch (OAuthNotAuthorizedException e) {
            throw new NetException(e);
        } catch (OAuthExpectationFailedException e) {
            throw new NetException(e);
        } catch (OAuthCommunicationException e) {
            throw new NetException(e);
        }
    }

    public void sign(HTTP http) throws NetException {

        try {
            consumer.sign(http.getConnection());
        } catch (OAuthMessageSignerException e) {
            throw new NetException(e);
        } catch (OAuthExpectationFailedException e) {
            throw new NetException(e);
        } catch (OAuthCommunicationException e) {
            throw new NetException(e);
        }
    }

    public String getConsumerKey() {

        return consumer.getConsumerKey();
    }

    public String getConsumerSecret() {

        return consumer.getConsumerSecret();
    }

    public String getToken() {

        return consumer.getToken();
    }

    public String getTokenSecret() {
        return consumer.getTokenSecret();
    }

    public void setTokenWithSecret(String token, String tokenSecret) {
        consumer.setTokenWithSecret(token, tokenSecret);
    }
}
