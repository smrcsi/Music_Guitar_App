package com.example.guitar_music_app

// Konstanty, compile time, efficient, slouzi k rendrovani do UI
    internal const val LOGIN_ERROR = "Problém s přihlášením."
    internal const val LOADING = "Načítání..."
    internal const val LOGOUT_ERROR = "Error logging out user."
    internal const val GET_RESULT_ERROR = "Error retrieving note."
    internal const val GET_RESULTS_ERROR = "Error retrieving notes."
    internal const val SIGN_OUT = "Odhlášení"
    internal const val SIGN_IN = "Přihlášení"
    internal const val SIGNED_IN = "Uživatel přihlášen"
    internal const val SIGNED_OUT = "Uživatel odhlášen"
    internal const val ERROR_NETWORK_UNAVAILABLE = "Network Unavailable"
    internal const val ERROR_AUTH = "An Error Has Occured"
    internal const val RETRY = "RETRY"
    internal const val ANTENNA_EMPTY = "ic_antenna_empty"
    internal const val ANTENNA_FULL = "ic_antenna_full"
    internal const val ANTENNA_LOOP = "antenna_loop_fast"

    /**
     * This value is just a constant to denote our sign in request; It can be any int.
     * Would have been great if that was explained in the docs, I assumed at first that it had to
     * be a specific value.
     */
    internal const val RC_SIGN_IN = 1337
