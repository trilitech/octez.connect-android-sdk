package io.tezos.octezconnect.blockchain.tezos.internal.creator

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.tezos.octezconnect.blockchain.tezos.data.TezosAppMetadata
import io.tezos.octezconnect.blockchain.tezos.data.TezosPermission
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.storage.MockSecureStorage
import io.tezos.octezconnect.core.internal.storage.MockStorage
import io.tezos.octezconnect.core.internal.storage.StorageManager
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator
import io.tezos.octezconnect.core.internal.utils.toHexString
import io.tezos.octezconnect.core.scope.BeaconScope
import kotlinx.coroutines.runBlocking
import mockTime
import org.junit.Before
import org.junit.Test
import permissionTezosRequest
import permissionTezosResponse
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class TezosCreatorTest {

    @MockK
    private lateinit var identifierCreator: IdentifierCreator

    private lateinit var storageManager: StorageManager

    private lateinit var creator: TezosCreator

    private val currentTimeMillis: Long = 1

    private val beaconScope: BeaconScope = BeaconScope.Global

    private val version: String = "2"
    private val senderId: String = "00"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockTime(currentTimeMillis)

        every { identifierCreator.accountId(any(), any()) } answers { Result.success(firstArg()) }
        every { identifierCreator.senderId(any()) } answers { Result.success(firstArg<ByteArray>().toHexString().asString()) }

        storageManager = StorageManager(beaconScope, MockStorage(), MockSecureStorage(), identifierCreator, BeaconConfiguration(ignoreUnsupportedBlockchains = false))
        creator = TezosCreator(
            DataTezosCreator(storageManager, identifierCreator),
            V1BeaconMessageTezosCreator(),
            V2BeaconMessageTezosCreator(),
            V3BeaconMessageTezosCreator(),
        )
    }

    @Test
    fun `extracts permission`() {
        val id = "id"

        val appMetadata = TezosAppMetadata(senderId, "mockApp")
        val permissionRequest = permissionTezosRequest(id = id, version = version, senderId = senderId)
        val permissionResponse = permissionTezosResponse(id = id, version = version, destination = Connection.Id.P2P("00"))

        runBlocking {
            storageManager.setAppMetadata(listOf(appMetadata))
            val permission = creator.data.extractOutgoingPermission(permissionRequest, permissionResponse).getOrThrow()

            val expected = listOf(TezosPermission(
                permissionResponse.account.accountId,
                appMetadata.senderId,
                currentTimeMillis,
                permissionResponse.account.address,
                permissionResponse.account.publicKey,
                permissionResponse.account.network,
                appMetadata,
                permissionResponse.scopes,
            ))

            assertEquals(expected, permission)
        }
    }


    @Test
    fun `fails to extract permission when app metadata not found`() {
        val id = "id"

        val permissionRequest = permissionTezosRequest(id = id, version = version)
        val permissionResponse = permissionTezosResponse(id = id, version = version)

        runBlocking {
            storageManager.setAppMetadata(emptyList())
        }

        assertFailsWith<IllegalStateException> {
            runBlocking { creator.data.extractOutgoingPermission(permissionRequest, permissionResponse).getOrThrow() }
        }
    }
}