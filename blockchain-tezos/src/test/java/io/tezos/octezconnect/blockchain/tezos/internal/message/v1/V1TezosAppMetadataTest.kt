package io.tezos.octezconnect.blockchain.tezos.internal.message.v1

import fromValues
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.tezos.octezconnect.blockchain.tezos.Tezos
import io.tezos.octezconnect.blockchain.tezos.data.TezosAppMetadata
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
import kotlinx.serialization.json.JsonObject
import mockDependencyRegistry
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class V1TezosAppMetadataTest {

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
        every { dependencyRegistry.storageManager } returns storageManager
        every { dependencyRegistry.identifierCreator } returns identifierCreator

        json = coreJson(dependencyRegistry.blockchainRegistry, CoreCompat(beaconScope))
    }

    @Test
    fun `is deserialized from JSON`() {
        listOf(expectedWithJson(), expectedWithJson(includeNulls = true), expectedWithJson(icon = "icon"))
            .map { json.decodeFromString<V1TezosAppMetadata>(it.second) to it.first }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    @Test
    fun `serializes to JSON`() {
        listOf(expectedWithJson(), expectedWithJson(icon = "icon"))
            .map { json.decodeFromString(JsonObject.serializer(), json.encodeToString(it.first)) to
                    json.decodeFromString(JsonObject.serializer(), it.second) }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    @Test
    fun `is created from AppMetadata`() {
        listOf(expectedWithAppMetadata(), expectedWithAppMetadata(icon = "icon"))
            .map { V1TezosAppMetadata.fromAppMetadata(it.second) to it.first }
            .forEach { assertEquals(it.second, it.first) }
    }

    @Test
    fun `converts to AppMetadata`() {
        listOf(expectedWithAppMetadata(), expectedWithAppMetadata(icon = "icon"))
            .map { it.first.toAppMetadata() to it.second }
            .forEach { assertEquals(it.second, it.first) }
    }

    private fun expectedWithJson(
        beaconId: String = "beaconId",
        name: String = "name",
        icon: String? = null,
        includeNulls: Boolean = false,
    ): Pair<V1TezosAppMetadata, String> {
        val values = mapOf(
            "beaconId" to beaconId,
            "name" to name,
            "icon" to icon,
        )

        val jsonObject = JsonObject.fromValues(values, includeNulls).toString()

        return V1TezosAppMetadata(beaconId, name, icon) to jsonObject
    }

    private fun expectedWithAppMetadata(
        beaconId: String = "beaconId",
        name: String = "name",
        icon: String? = null,
    ): Pair<V1TezosAppMetadata, TezosAppMetadata> =
        V1TezosAppMetadata(beaconId, name, icon) to TezosAppMetadata(beaconId, name, icon)
}