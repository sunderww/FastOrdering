//
//  DishCell.m
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "DishCell.h"

@implementation DishCell

- (void)awakeFromNib {
  // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
  [super setSelected:selected animated:animated];
  
  if (selected) {
    self.textField.text = @"";
    [self.textField becomeFirstResponder];
  }
}

- (void)setDelegate:(id<UITextFieldDelegate>)delegate {
  _delegate = delegate;
  self.textField.delegate = delegate;
}

- (void)setDish:(Dish *)dish andTag:(NSUInteger)tag {
  mainLabel.text = dish.name;
  self.textField.text = @"0";
  self.textField.tag = tag;
}

@end
