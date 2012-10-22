package casmi.timeline;

public     class Dissolve {

    private double time;

    private DissolveMode mode = DissolveMode.CROSS;

    public Dissolve(DissolveMode mode, double time) {
        this.setTime(time);
        this.setMode(mode);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

	public DissolveMode getMode() {
		return mode;
	}

	public void setMode(DissolveMode mode) {
		this.mode = mode;
	}
}