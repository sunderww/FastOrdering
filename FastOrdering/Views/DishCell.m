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

- (void)setDelegate:(id<DishCellDelegate>)delegate {
  _delegate = delegate;
  self.textField.delegate = delegate;
}

- (void)setDish:(Dish *)dish andTag:(NSUInteger)tag {
  mainLabel.text = dish.name;
  self.textField.text = @"0";
  self.textField.tag = tag;
}

- (void)setQuantity:(NSInteger)quantity {
	self.textField.text = [NSString stringWithFormat:@"%ld", (long)quantity];
}

- (void)setEditable:(BOOL)editable {
	if (editButton.hidden != editable) return; // nothing to change
	
	editButton.hidden = !editable;
	CGRect frame = mainLabel.frame;
	float delta = editButton.frame.size.width * (editable ? 1 : -1);
	frame.origin.x += delta;
	frame.size.width -= delta;
	mainLabel.frame = frame;
}

- (IBAction)edit {
	[self.delegate editButtonClickedForDishCell:self];
}

@end
