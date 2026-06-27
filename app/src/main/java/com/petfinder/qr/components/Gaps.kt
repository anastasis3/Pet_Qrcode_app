package com.petfinder.qr.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/** Vertical spacer. */
@Composable
fun VGap(height: Dp) = Spacer(modifier = Modifier.height(height))

/** Horizontal spacer. */
@Composable
fun HGap(width: Dp) = Spacer(modifier = Modifier.width(width))
