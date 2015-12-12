//
//  LoaderView.m
//  Quizz
//
//  Created by Sunder on 09/12/13.
//  Copyright (c) 2013 Sunder. All rights reserved.
//

#import <CoreGraphics/CoreGraphics.h>
#import "LoaderView.h"
#import "AppDelegate.h"

@implementation LoaderView

- (void)baseInit {
  [[NSBundle mainBundle] loadNibNamed:@"LoaderView" owner:self options:nil];
  centerView.layer.cornerRadius = 7;
  centerView.layer.borderWidth = 2;
  //    centerView.layer.borderColor = kAppBlueColor.CGColor;
  
  animated = NO;
  [self animate:YES];
  
  [self addSubview:view];
}

- (void)reinit {
  NSString * title = [NSString stringWithFormat:@"%@...", NSLocalizedString(@"Loading", @"")];
  textLabel.text = self.title.length ? self.title : title;
  if (self.moved) {
    CGRect frame = centerView.frame;
    frame.origin.y += 70;
    centerView.frame = frame;
  }
}

- (id)initWithFrame:(CGRect)frame
{
  self = [super initWithFrame:frame];
  if (self) {
    [self baseInit];
  }
  return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder {
  self = [super initWithCoder:aDecoder];
  if (self) {
    [self baseInit];
  }
  return self;
}

- (void)animate:(BOOL)flag {
  if (flag == animated)
    return;
  
  animated = flag;
}

@end
