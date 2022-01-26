
#### ClickRuneBlock `CLICK_RUNE_BLOCK`
* Identifier: `tactus`
* Type: `Trigger`
* Stack: `-0/+0`
* Note: Activates all adjacent runes when right-clicked by player
* Throws: Never

#### TransferRuneBlock `NOOP_RUNE_BLOCK`
* Identifier: `vanitas`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Transmits the signal with no alteration 
* Throws: Never

#### RedstoneDigitalOutputRuneBlock `REDSTONE_DIGITAL_OUT_RUNE_BLOCK`
* Identifier: `exitus`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Transmits the signal with no alteration, emits a redstone signal of strength 15 while active
* Throws: Never

#### ScatterRuneBlock `SCATTER_RUNE_BLOCK`
* Identifier: `dispergat`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Transmits the signal to all adjacent runes with no alteration
* Throws: Never

#### RedirectRuneBlock `DIRECTION_RUNE_BLOCK`
* Identifier: `directio`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Transmits the signal to the faced rune block
* Throws: Never

#### RandomRuneBlock `RANDOM_UPDATE_RUNE_BLOCK`
* Identifier: `fortuitus`
* Type: `Trigger`
* Stack: `-0/+0`
* Note: Activates all adjacent runes when randomly ticked
* Throws: Never

#### ForkRuneBlock `FORK_RUNE_BLOCK`
* Identifier: `furca`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Transmits the signal to the faced rune block, and continues in the current direction
* Throws: Never

#### NumberRuneBlock `(MINUS/ZERO/ONE/TWO/THREE/FOUR/FIVE)_RUNE_BLOCK`
* Identifier: `numerus_*`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Creates the base-6 number rune instance
* Throws: Never

#### LogicRuneBlock `PUSH_RUNE_BLOCK`
* Identifier: `paello`
* Type: `Transfer`
* Stack: `-0/+1`
* Note: Appends `value` onto the stack
* Throws: Never

#### LogicRuneBlock `PULL_RUNE_BLOCK`
* Identifier: `divexo`
* Type: `Transfer`
* Stack: `-1/+0`
* Note: Removes element from the stack, and into `value`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `EXCHANGE_RUNE_BLOCK`
* Identifier: `cambitas`
* Type: `Transfer`
* Stack: `-1/+1`
* Note: Exchanges the two top stack values
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `DUPLICATE_RUNE_BLOCK`
* Identifier: `effingo`
* Type: `Transfer`
* Stack: `-0/+1`
* Note: Duplicates the top stack value
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `OR_RUNE_BLOCK`
* Identifier: `vel`
* Type: `Transfer`
* Stack: `-2/+1`
* Note: Performs a boolean OR on tow top stack values, and replaces them with the result
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `ADD_RUNE_BLOCK`
* Identifier: `adaugeo`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Adds the `value` to the top stack element
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `NOT_RUNE_BLOCK`
* Identifier: `ni`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Replaces top stack element with `1` if it's not `0`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `MULTIPLY_RUNE_BLOCK`
* Identifier: `multiplico`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Multiples the top stack element by `value`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `INVERT_RUNE_BLOCK`
* Identifier: `inverto`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Inverts the top stack element (e.g. `1` -> `-1`, `-1` -> `1`)
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `RECIPROCAL_RUNE_BLOCK`
* Identifier: `mutuus`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Sets the top stack element to the result of dividing `1` by it
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `EQUALS_RUNE_BLOCK`
* Identifier: `pares`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Checks if the top stack element is equal with `value`, and sets it to either `1` or `0`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `LESS_RUNE_BLOCK`
* Identifier: `minus`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Checks if the top stack element is smaller than `value`, and sets it to either `1` or `0`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `MORE_RUNE_BLOCK`
* Identifier: `magis`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Checks if the top stack element is bigger than `value`, and sets it to either `1` or `0`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `INCREMENT_RUNE_BLOCK`
* Identifier: `incrementum`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Increments the top stack element by `1`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `DECREMENT_RUNE_BLOCK`
* Identifier: `decrementum`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Decrements the top stack element by `1`
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `POP_RUNE_BLOCK`
* Identifier: `absumo`
* Type: `Transfer`
* Stack: `-1/+0`
* Note: Removes one element from the stack
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `RANDOM_RUNE_BLOCK`
* Identifier: `temere`
* Type: `Transfer`
* Stack: `-0/+1`
* Note: Adds a random number in range of 0 (inclusive) to 1 (exclusive) to the stack
* Throws: `INVALID_ARGUMENT`

#### LogicRuneBlock `SINE_RUNE_BLOCK`
* Identifier: `sine`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Replaces the top stack element with value returned by the SIN function of it
* Throws: `INVALID_ARGUMENT`

#### IfRuneBlock `IF_RUNE_BLOCK`
* Identifier: `si`
* Type: `Transfer`
* Stack: `-1/+0` (if value cannot be pulled the error is ignored)
* Note: Passes the execution to the faced rune if the top element on stack is not equal 0, otherwise continues
* Throws: Never

#### PlaceRuneBlock `PLACE_RUNE_BLOCK`
* Identifier: `locus`
* Type: `Transfer`
* Stack: `-3/+0`
* Note: Places a block at the designated (relative) XYZ coordinates, with maximum distance of 256 blocks
* Throws: `INVALID_ARGUMENT`

#### PlayerRuneBlock `PROXIMITY_SENSOR_RUNE_BLOCK`
* Identifier: `propinquitas`
* Type: `Transfer`
* Stack: `-0/+1`
* Note: Adds `1` to stack if a player is in range, otherwise adds `0` to the stack
* Throws: Never

#### RedstoneAnalogOutputRuneBlock `REDSTONE_ANALOG_OUT_RUNE_BLOCK`
* Identifier: `scribo`
* Type: `Transfer`
* Stack: `-1/+0`
* Note: Pulls a value from stack and outputs is as redstone signal
* Throws: `INVALID_ARGUMENT_COUNT`

#### SplitRuneBlock `SPLIT_RUNE_BLOCK`
* Identifier: `scindo`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Splits the signal to the faced and opposite rune
* Throws: Never

#### RedstoneEntryRuneBlock `REDSTONE_DIGITAL_IN_RUNE_BLOCK`
* Identifier: `evigilo`
* Type: `Trigger`
* Stack: `-0/+0`
* Note: Triggers execution when powered
* Throws: Never

#### RedstoneAnalogInputRuneBlock `REDSTONE_ANALOG_IN_RUNE_BLOCK`
* Identifier: `evigilo`
* Type: `Transfer`
* Stack: `-0/+1`
* Note: Reads redstone signal from the environment and stores it on the stack
* Throws: Never

#### ClockRuneBlock `CLOCK_RUNE_BLOCK`
* Identifier: `horologium`
* Type: `Trigger`
* Stack: `-0/+0`
* Note: Triggers execution periodically
* Throws: Never

#### RedstoneGateRuneBlock `GATEWAY_RUNE_BLOCK`
* Identifier: `porta`
* Type: `Transfer`
* Stack: `-0/+0`
* Note: Passes execution when powered with redstone, blocks it otherwise
* Throws: Never

#### JoinRuneBlock `JOIN_RUNE_BLOCK`
* Identifier: `adiungo`
* Type: `Transfer`
* Stack: `-0/+?`
* Note: When no triggered along the facing axis, it stores the stack and ends execution, when later triggered along the facing axis is appends that stack
* Throws: Never