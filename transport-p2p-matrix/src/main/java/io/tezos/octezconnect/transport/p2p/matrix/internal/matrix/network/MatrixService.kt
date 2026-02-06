package io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network

import io.tezos.octezconnect.core.internal.network.HttpClient
import io.tezos.octezconnect.transport.p2p.matrix.internal.BeaconP2pMatrixConfiguration

internal abstract class MatrixService(protected val httpClient: HttpClient) {

    protected inline fun <T> withApiBase(node: String, action: (baseUrl: String) -> T): T = action(apiUrl(node))
    protected inline fun <T> withApi(node: String, action: (baseUrl: String) -> T): T =
        withApiBase(node) { action("$it/${BeaconP2pMatrixConfiguration.MATRIX_API_VERSION.trimStart('/')}") }

    private fun apiUrl(node: String): String = "https://$node/${BeaconP2pMatrixConfiguration.MATRIX_API_BASE.trimStart('/')}"
}