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

    // Configure the view for the selected state
}

- (void)setDelegate:(id<UITextFieldDelegate>)delegate {
  _delegate = delegate;
  textField.delegate = delegate;
}

- (void)setDish:(Dish *)dish andTag:(NSUInteger)tag {
  mainLabel.text = dish.name;
  textField.text = @"0";
  textField.tag = tag;
}

@end
