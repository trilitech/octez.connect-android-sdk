package io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.room

import io.tezos.octezconnect.core.internal.network.HttpClient
import io.tezos.octezconnect.core.internal.network.HttpClient.Companion.post
import io.tezos.octezconnect.core.internal.network.data.ApplicationJson
import io.tezos.octezconnect.core.internal.network.data.BearerHeader
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.data.api.MatrixError
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.data.api.room.*
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.MatrixService
import kotlinx.coroutines.flow.single

internal class MatrixRoomService(httpClient: HttpClient) : MatrixService(httpClient) {

    suspend fun createRoom(
        node: String,
        accessToken: String,
        roomConfig: MatrixCreateRoomRequest = MatrixCreateRoomRequest(),
    ): Result<MatrixCreateRoomResponse> =
        withApi(node) { baseUrl ->
            httpClient.post<MatrixCreateRoomRequest, MatrixCreateRoomResponse, MatrixError>(
                baseUrl,
                "/createRoom",
                body = roomConfig,
                headers = listOf(BearerHeader(accessToken), ApplicationJson()),
            ).single()
        }

    suspend fun inviteToRoom(
        node: String,
        accessToken: String,
        user: String,
        roomId: String,
    ): Result<MatrixInviteRoomResponse> =
        withApi(node) { baseUrl ->
            httpClient.post<MatrixInviteRoomRequest, MatrixInviteRoomResponse, MatrixError>(
                baseUrl,
                "/rooms/$roomId/invite",
                body = MatrixInviteRoomRequest(user),
                headers = listOf(BearerHeader(accessToken), ApplicationJson()),
            ).single()
        }

    suspend fun joinRoom(
        node: String,
        accessToken: String,
        roomId: String,
    ): Result<MatrixJoinRoomResponse> =
        withApi(node) { baseUrl ->
            httpClient.post<MatrixJoinRoomRequest, MatrixJoinRoomResponse, MatrixError>(
                baseUrl,
                "/rooms/$roomId/join",
                body = MatrixJoinRoomRequest(),
                headers = listOf(BearerHeader(accessToken), ApplicationJson()),
            ).single()
        }
}