# TODO

- Make PGNTest able to validate no lines between tagpairs and move list
- Add basic chess move tests

# Done
- Why isn't any testing performed to verify that the move generator actually rejects illegal move instances?
This would have forced IBoard.toMove to return illegal moves. Move generator implementers would then have to take care of implementing illegal moves support ... which doesn't seem useful.
I preferred to test illegal moves in their UCI representation, which seems a more plausible scenario.

- What happens if Chess960Test is in test suite and adapter has no @Supports annotation?
  An IllegalStateException is thrown with a quite explicit message.
