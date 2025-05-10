class HealCommand implements ItemEffectCommand {
    private int healAmount;

    public HealCommand(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void execute(PlayerTank playerTank) {
        playerTank.heal(healAmount);
    }
}
