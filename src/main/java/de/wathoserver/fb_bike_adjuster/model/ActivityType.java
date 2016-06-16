package de.wathoserver.fb_bike_adjuster.model;

public enum ActivityType {

  RIDING_IN_A_BUS(16016);

  private int id;

  private ActivityType(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
