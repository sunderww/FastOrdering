//
//  CategoryExpandableCell.m
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "CategoryExpandableCell.h"

@implementation CategoryExpandableCell

- (void)initialize {
	arrow = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"arrow-right"]];
	arrow.autoresizingMask = UIViewAutoresizingFlexibleBottomMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleLeftMargin;
	arrow.center = CGPointMake(arrow.frame.size.width / 2., arrow.frame.size.height / 2.);

	CGRect frame = arrow.frame;
	frame.origin.x = self.frame.size.width - (frame.size.width + 10);
	frame.origin.y = (self.frame.size.height - frame.size.height) / 2.;
	arrow.frame = frame;

	[self addSubview:arrow];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
	[super setSelected:selected animated:animated];
	
	// Configure the view for the selected state
}

- (void)setExpansionStyle:(UIExpansionStyle)expansionStyle animated:(BOOL)animated {
	if (expansionStyle != _expansionStyle) {
		_expansionStyle = expansionStyle;
		[UIView animateWithDuration:0.3 animations:^{
			float rotation = self.expansionStyle == UIExpansionStyleCollapsed ? 0 : M_PI_2;
			arrow.transform = CGAffineTransformMakeRotation(rotation);
		}];

		[self updateData];
	}
}

- (void)setCategory:(DishCategory *)category {
	_category = category;
	[self updateData];
}

- (void)setComposition:(MenuComposition *)composition {
	_composition = composition;
	[self updateData];
}

- (void)updateData {
	if (self.category)
		self.textLabel.text = self.category.name;
	else
		self.textLabel.text = self.composition.name;
}

@end
