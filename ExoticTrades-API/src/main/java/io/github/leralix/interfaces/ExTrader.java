package io.github.leralix.interfaces;

import org.leralix.lib.position.Vector3D;

import java.util.List;

public interface ExTrader {

    String getID();

    String getName();

    List<ExRareItem> getItemsSold();

    Vector3D getCurrentPosition();

    List<Vector3D> getPotentialPosition();

    int getNbHoursBeforeNextPosition();

}
