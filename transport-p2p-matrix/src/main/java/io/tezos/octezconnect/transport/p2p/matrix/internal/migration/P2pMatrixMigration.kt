package io.tezos.octezconnect.transport.p2p.matrix.internal.migration

import io.tezos.octezconnect.core.internal.migration.Migration

internal suspend fun Migration.migrateMatrixRelayServer(matrixNodes: List<String>): Result<Unit> =
    migrate(MatrixMigrationTarget.RelayServer(matrixNodes))

internal sealed interface MatrixMigrationTarget : Migration.Target {
    data class RelayServer(val nodes: List<String>) : MatrixMigrationTarget {
        override val identifier: String = IDENTIFIER

        companion object {
            const val IDENTIFIER = "matrixRelayServer"
        }
    }
}