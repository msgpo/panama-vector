/*
 * Copyright (c) 2006, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.net.httpserver;

/**
 * Authenticator represents an implementation of an HTTP authentication
 * mechanism. Sub-classes provide implementations of specific mechanisms
 * such as Digest or Basic auth. Instances are invoked to provide verification
 * of the authentication information provided in all incoming requests.
 * Note. This implies that any caching of credentials or other authentication
 * information must be done outside of this class.
 */
public abstract class Authenticator {

    /**
     * Constructor for subclasses to call.
     */
    protected Authenticator () { }

    /**
     * Base class for return type from authenticate() method
     */
    public abstract static class Result {

        /**
         * Constructor for subclasses to call.
         */
        protected Result () {}
    }



    /**
     * Indicates an authentication failure. The authentication
     * attempt has completed.
     */
    public static class Failure extends Result {

        private int responseCode;

        /**
         * Creates a {@code Failure} instance with given response code.
         *
         * @param responseCode The response code to associate with this
         *                     {@code Failure} instance
         */
        public Failure (int responseCode) {
            this.responseCode = responseCode;
        }

        /**
         * returns the response code to send to the client
         *
         * @return The response code associated with this {@code Failure} instance
         */
        public int getResponseCode() {
            return responseCode;
        }
    }

    /**
     * Indicates an authentication has succeeded and the
     * authenticated user principal can be acquired by calling
     * getPrincipal().
     */
    public static class Success extends Result {
        private HttpPrincipal principal;

        /**
         * Creates a {@code Success} instance with given {@code Principal}.
         *
         * @param p The authenticated user you wish to set as Principal
         */
        public Success (HttpPrincipal p) {
            principal = p;
        }
        /**
         * returns the authenticated user Principal
         *
         * @return The {@code Principal} instance associated with the authenticated user
         *
         */
        public HttpPrincipal getPrincipal() {
            return principal;
        }
    }

    /**
     * Indicates an authentication must be retried. The
     * response code to be sent back is as returned from
     * getResponseCode(). The Authenticator must also have
     * set any necessary response headers in the given HttpExchange
     * before returning this Retry object.
     */
    public static class Retry extends Result {

        private int responseCode;

        /**
         * Creates a {@code Retry} instance with given response code.
         *
         * @param responseCode The response code to associate with this
         *                     {@code Retry} instance
         */
        public Retry (int responseCode) {
            this.responseCode = responseCode;
        }

        /**
         * returns the response code to send to the client
         *
         * @return The response code associated with this {@code Retry} instance
         */
        public int getResponseCode() {
            return responseCode;
        }
    }

    /**
     * called to authenticate each incoming request. The implementation
     * must return a Failure, Success or Retry object as appropriate :-
     * <p>
     * Failure means the authentication has completed, but has failed
     * due to invalid credentials.
     * <p>
     * Sucess means that the authentication
     * has succeeded, and a Principal object representing the user
     * can be retrieved by calling Sucess.getPrincipal() .
     * <p>
     * Retry means that another HTTP exchange is required. Any response
     * headers needing to be sent back to the client are set in the
     * given HttpExchange. The response code to be returned must be provided
     * in the Retry object. Retry may occur multiple times.
     *
     * @param exch The HttpExchange upon which authenticate is called
     * @return The result
     */
    public abstract Result authenticate (HttpExchange exch);
}
