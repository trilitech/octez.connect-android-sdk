package io.tezos.octezconnect.blockchain.substrate.data

import fromValues
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.tezos.octezconnect.blockchain.substrate.Substrate
import io.tezos.octezconnect.blockchain.substrate.internal.creator.*
import io.tezos.octezconnect.blockchain.substrate.internal.serializer.*
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
import kotlinx.serialization.json.JsonObject
import mockDependencyRegistry
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class SubstrateNetworkTest {

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
        val substrate = Substrate(
            SubstrateCreator(
                DataSubstrateCreator(storageManager, identifierCreator),
                V1BeaconMessageSubstrateCreator(),
                V2BeaconMessageSubstrateCreator(),
                V3BeaconMessageSubstrateCreator(),
            ),
            SubstrateSerializer(
                DataSubstrateSerializer(),
                V1BeaconMessageSubstrateSerializer(),
                V2BeaconMessageSubstrateSerializer(),
                V3BeaconMessageSubstrateSerializer(),
            ),
        )

        dependencyRegistry = mockDependencyRegistry(substrate)
        every { dependencyRegistry.storageManager } returns storageManager
        every { dependencyRegistry.identifierCreator } returns identifierCreator

        json = coreJson(dependencyRegistry.blockchainRegistry, CoreCompat(beaconScope))
    }

    @Test
    fun `is deserialized from JSON`() {
        listOf(
            expectedWithJsonStrings(),
            expectedWithJsonStrings(includeNulls = true),
            expectedWithJsonStrings(name = "name"),
            expectedWithJsonStrings(rpcUrl = "rpcUrl"),
            expectedWithJsonStrings(name = "name", rpcUrl = "rpcUrl"),
        ).map {
            json.decodeFromString<SubstrateNetwork>(it.second) to it.first
        }.forEach {
            assertEquals(it.second, it.first)
        }
    }

    @Test
    fun `serializes to JSON`() {
        listOf(
            expectedWithJsonStrings(),
            expectedWithJsonStrings(name = "name"),
            expectedWithJsonStrings(rpcUrl = "rpcUrl"),
            expectedWithJsonStrings(name = "name", rpcUrl = "rpcUrl"),
        ).map {
            json.decodeFromString(JsonObject.serializer(), json.encodeToString(it.first)) to
                    json.decodeFromString(JsonObject.serializer(), it.second)
        }.forEach {
            assertEquals(it.second, it.first)
        }
    }

    private fun expectedWithJsonStrings(
        genesisHash: String = "genesisHash",
        name: String? = null,
        rpcUrl: String? = null,
        includeNulls: Boolean = false,
    ): Pair<SubstrateNetwork, String> {
        val values = mapOf(
            "genesisHash" to genesisHash,
            "name" to name,
            "rpcUrl" to rpcUrl,
        )

        val jsonObject = JsonObject.fromValues(values, includeNulls).toString()

        return SubstrateNetwork(genesisHash, name, rpcUrl) to jsonObject
    }
}