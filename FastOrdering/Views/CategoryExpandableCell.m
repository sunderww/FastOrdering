//
//  CategoryExpandableCell.m
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "CategoryExpandableCell.h"

@implementation CategoryExpandableCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setExpansionStyle:(UIExpansionStyle)expansionStyle animated:(BOOL)animated {
  if (expansionStyle != _expansionStyle) {
    _expansionStyle = expansionStyle;
    [self updateData];
  }
}

- (void)setCategory:(DishCategory *)category {
  _category = category;
  [self updateData];
}

- (void)updateData {
  self.textLabel.text = self.category.name;
}

@end
