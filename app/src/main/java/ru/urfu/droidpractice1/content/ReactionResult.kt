package ru.urfu.droidpractice1.content

data class ReactionResult(
    val likes: Int,
    val dislikes: Int,
    val selection: Reaction
)

fun calculateReaction(
    currentSelection: Reaction,
    target: Reaction,
    likes: Int,
    dislikes: Int
): ReactionResult {
    var newLikes = likes
    var newDislikes = dislikes
    val newSelection: Reaction

    if (currentSelection == target) {
        if (target == Reaction.LIKE) newLikes-- else newDislikes--
        newSelection = Reaction.NONE
    } else {
        if (currentSelection == Reaction.LIKE) newLikes--
        if (currentSelection == Reaction.DISLIKE) newDislikes--
        if (target == Reaction.LIKE) newLikes++ else newDislikes++
        newSelection = target
    }

    return ReactionResult(newLikes, newDislikes, newSelection)
}