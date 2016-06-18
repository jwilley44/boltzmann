package willey.lib.physics.polymer.interactor;

import willey.lib.math.MathUtil;

public enum MoveAcceptor
{
	Hard
	{
		@Override
		boolean acceptMove(double pInteractionRadius, double pDistance)
		{
			return MathUtil.getThreadLocal().nextDouble() < Math.exp(1 /((pInteractionRadius - pDistance) - 1));
		}
	},
	Soft
	{
		@Override
		boolean acceptMove(double pInteractionRadius, double pDistance)
		{
			return pDistance > pInteractionRadius;
		}
	};
	
	abstract boolean acceptMove(double pInteractionRadius, double pDistance);
}
