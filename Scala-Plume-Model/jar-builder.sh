#!/usr/bin/env bash
# Brodderick Rodriguez
# Auburn University - CSSE
# 05 Feb. 2019

E_N=plume-scala   # extension name
E_D=nlogo-model   # extension directory
N_V=6.0.4         # NetLogo version
N_D=/Applications/NetLogo\ $N_V/NetLogo\ $N_V.app # NetLogo path

clear
echo "starting jar-builder..."
echo "killing NetLogo (unsafe)..."
killall NetLogo $N_V    # 'killall' is essentially force quit

echo "removing old compiled files..."
rm -rf target
rm -f $E_D/$E_N/$E_N.jar
rm -f *.jar

echo "creating .jar..."
sbt clean compile package

echo "moving .jar to $E_N extension directory..."
mkdir $E_D/$E_N
mv $E_N.jar $E_D/$E_N

echo "launching NetLogo..."
open -a /Applications/NetLogo\ $N_V/NetLogo\ $N_V.app ./$E_D/plume_extended.nlogo

echo "jar-builder done"
