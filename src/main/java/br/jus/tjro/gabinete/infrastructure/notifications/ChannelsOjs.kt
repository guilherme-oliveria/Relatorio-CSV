package br.jus.tjro.gabinete.infrastructure.notifications

class ChannelsOjs(chatsOjs: String) {

    var channels: List<ChannelOjs>

    init {
        val regex = "((-?\\d+)\\s?->\\s?([\\d\\s\\w(all)-]+)[, ]*)".toRegex()
        this.channels = regex.findAll(chatsOjs).map{ it.destructured }
                                               .map {
                                                   val ojs =
                                                           if (it.component3().contains("all")) listOf("-42")
                                                           else it.component3().split(" ").map { oj -> oj }
                                                   ChannelOjs(it.component2(), ojs)
                                               }
                                               .toList()
    }

}

typealias Channel = String
typealias Ojs = List<String>

class ChannelOjs(val channel: Channel, val ojs: Ojs)
