package eu.swdev.stream

import akka.stream._
import akka.stream.scaladsl.GraphDSL
import akka.stream.stage.{GraphStageLogic, GraphStage}

import scala.collection.immutable
import scala.collection.mutable.ArrayBuffer


trait MuxAndDemuxBuilder[In, Out] {
  def addChannel(): Graph[FlowShape[In, Out], Unit]
  def connect(): Graph[FlowShape[(Int, Out), (Int, In)], Unit]
}

object MuxAndDemuxBuilder {

  def apply[In, Out]() = new MuxAndDemuxBuilder[In, Out] {

    val channels = ArrayBuffer.empty[(Inlet[In], Outlet[Out])]

    override def addChannel(): Graph[FlowShape[In, Out], Unit] = {
      val in = Inlet[In](s"muxAndDemux.in.${channels.size}")
      val out = Outlet[Out](s"muxAndDemux.out.${channels.size}")
      channels += ((in, out))
      GraphDSL.create() { implicit b =>
        FlowShape.of(in, out)
      }
    }

    override def connect(): Graph[FlowShape[(Int, Out), (Int, In)], Unit] = {
      return new MuxAndDemuxer(channels)
    }


  }

  class MuxAndDemuxer[In, Out](val channels: ArrayBuffer[(Inlet[In], Outlet[Out])]) extends GraphStage[FlowShape[(Int, Out), (Int, In)]] {

    val muxOut = Outlet[(Int, In)]("muxAndDemux.mux")
    val demuxIn = Inlet[(Int, Out)]("muxAndDemux.demux")

    override val shape: FlowShape[(Int, Out), (Int, In)] = FlowShape.of(demuxIn, muxOut)

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

      private var pendingCount = channels.size

      ???


    }

  }


}

