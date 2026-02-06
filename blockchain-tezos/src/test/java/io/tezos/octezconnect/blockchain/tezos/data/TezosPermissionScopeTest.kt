package io.tezos.octezconnect.blockchain.tezos.data

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.tezos.octezconnect.blockchain.tezos.Tezos
import io.tezos.octezconnect.blockchain.tezos.internal.creator.*
import io.tezos.octezconnect.blockchain.tezos.internal.serializer.*
import io.tezos.octezconnect.blockchain.tezos.internal.wallet.TezosWallet
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.compat.CoreCompat
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.serializer.coreJson
import io.tezos.octezconnect.core.internal.storage.MockSecureStorage
import io.tezos.octezconnect.core.internal.storage.MockStorage
import io.tezos.octezconnect.core.internal.storage.StorageManager
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator
import io.tezos.octezconnect.core.scope.BeaconScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import mockDependencyRegistry
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class TezosPermissionScopeTest {

    @MockK
    private lateinit var wallet: TezosWallet

    @MockK
    private lateinit var identifierCreator: IdentifierCreator

    private lateinit var dependencyRegistry: DependencyRegistry
    private lateinit var storageManager: StorageManager

    private lateinit var json: Json

    private val beaconScope: BeaconScope = BeaconScope.Global

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        storageManager = StorageManager(beaconScope, MockStorage(), MockSecureStorage(), identifierCreator, BeaconConfiguration(ignoreUnsupportedBlockchains = false))
        val tezos = Tezos(
            wallet,
            TezosCreator(
                DataTezosCreator(storageManager, identifierCreator),
                V1BeaconMessageTezosCreator(),
                V2BeaconMessageTezosCreator(),
                V3BeaconMessageTezosCreator(),
            ),
            TezosSerializer(
                DataTezosSerializer(),
                V1BeaconMessageTezosSerializer(),
                V2BeaconMessageTezosSerializer(),
                V3BeaconMessageTezosSerializer(),
            ),
        )

        dependencyRegistry = mockDependencyRegistry(tezos)
        json = coreJson(dependencyRegistry.blockchainRegistry, CoreCompat(beaconScope))
    }

    @Test
    fun `is deserialized from string`() {
        expectedWithJsonValues()
            .map { json.decodeFromString<TezosPermission.Scope>(it.second) to it.first }
            .forEach { assertEquals(it.second, it.first) }
    }

    @Test
    fun `serializes to string`() {
        expectedWithJsonValues()
            .map { json.encodeToString(it.first) to it.second }
            .forEach { assertEquals(it.second, it.first) }
    }

    @Test
    fun `is deserialized from list of strings`() {
        expectedWithJsonArray()
            .map { json.decodeFromString<List<TezosPermission.Scope>>(it.second) to it.first }
            .forEach { assertEquals(it.second, it.first) }
    }

    @Test
    fun `serializes to list of string`() {
        expectedWithJsonArray()
            .map { json.decodeFromString(JsonArray.serializer(), json.encodeToString(it.first)) to
                    json.decodeFromString(JsonArray.serializer(), it.second) }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    private fun expectedWithJsonValues(): List<Pair<TezosPermission.Scope, String>> = listOf(
        TezosPermission.Scope.Sign to "\"sign\"",
        TezosPermission.Scope.OperationRequest to "\"operation_request\"",
    )

    private fun expectedWithJsonArray(): List<Pair<List<TezosPermission.Scope>, String>> = listOf(
        listOf(TezosPermission.Scope.Sign, TezosPermission.Scope.OperationRequest) to
                """
                    [
                        "sign", 
                        "operation_request" 
                    ]
                """.trimIndent(),
    )
}