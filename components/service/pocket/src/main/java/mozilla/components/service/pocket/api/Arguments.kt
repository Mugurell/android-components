/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import java.util.regex.Pattern

/**
 * A collection of shared argument validation functions.
 */
internal object Arguments {
    fun assertIsNotBlank(input: String, exceptionIdentifier: String) {
        require(input.isNotBlank()) { "Expected non-blank $exceptionIdentifier" }
    }

    fun assertApiKeyHasValidStructure(input: String) {
        require(Pattern.compile("[0-9]{5}-[a-zA-Z0-9]{24}").matcher(input).matches()) {
            "Provided api key does not have a valid structure"
        }
    }
}
