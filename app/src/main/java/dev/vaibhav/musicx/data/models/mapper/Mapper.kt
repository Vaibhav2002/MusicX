package dev.vaibhav.musicx.data.models.mapper

interface Mapper<Network, Domain> {

    fun toDomain(network: Network): Domain

    fun toDomainList(networks: List<Network>): List<Domain>

    fun toNetwork(domain: Domain): Network

    fun toNetworkList(domains: List<Domain>): List<Network>
}
